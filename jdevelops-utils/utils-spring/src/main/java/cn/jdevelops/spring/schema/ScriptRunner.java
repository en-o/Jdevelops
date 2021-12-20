//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jdevelops.spring.schema;

import cn.jdevelops.spring.exception.RuntimeSqlException;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.*;

/**
 * @author tnnn
 */
public class ScriptRunner {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String DEFAULT_DELIMITER = ";";
    private Connection connection;
    private boolean stopOnError;
    private boolean autoCommit;
    private boolean sendFullScript;
    private boolean removeCrs;
    private boolean escapeProcessing = true;
    private PrintWriter logWriter;
    private PrintWriter errorLogWriter;
    private String delimiter;
    private boolean fullLineDelimiter;

    public ScriptRunner(Connection connection) {
        this.logWriter = new PrintWriter(System.out);
        this.errorLogWriter = new PrintWriter(System.err);
        this.delimiter = ";";
        this.fullLineDelimiter = false;
        this.connection = connection;
    }

    public void setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setSendFullScript(boolean sendFullScript) {
        this.sendFullScript = sendFullScript;
    }

    public void setRemoveCrs(boolean removeCrs) {
        this.removeCrs = removeCrs;
    }

    public void setEscapeProcessing(boolean escapeProcessing) {
        this.escapeProcessing = escapeProcessing;
    }

    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setFullLineDelimiter(boolean fullLineDelimiter) {
        this.fullLineDelimiter = fullLineDelimiter;
    }

    public void runScript(Reader reader) {
        this.setAutoCommit();

        try {
            if (this.sendFullScript) {
                this.executeFullScript(reader);
            } else {
                this.executeLineByLine(reader);
            }
        } finally {
            this.rollbackConnection();
        }

    }

    private void executeFullScript(Reader reader) {
        StringBuilder script = new StringBuilder();

        String line;
        try {
            BufferedReader lineReader = new BufferedReader(reader);

            while((line = lineReader.readLine()) != null) {
                script.append(line);
                script.append(LINE_SEPARATOR);
            }

            String command = script.toString();
            this.println(command);
            this.executeStatement(command);
            this.commitConnection();
        } catch (Exception var6) {
            line = "Error executing: " + script + ".  Cause: " + var6;
            this.printlnError(line);
            throw new RuntimeSqlException(line, var6);
        }
    }

    private void executeLineByLine(Reader reader) {
        StringBuilder command = new StringBuilder();

        String line;
        try {
            for(BufferedReader lineReader = new BufferedReader(reader); (line = lineReader.readLine()) != null; command = this.handleLine(command, line)) {
            }

            this.commitConnection();
            this.checkForMissingLineTerminator(command);
        } catch (Exception var5) {
            line = "Error executing: " + command + ".  Cause: " + var5;
            this.printlnError(line);
            throw new RuntimeSqlException(line, var5);
        }
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private void setAutoCommit() {
        try {
            if (this.autoCommit != this.connection.getAutoCommit()) {
                this.connection.setAutoCommit(this.autoCommit);
            }

        } catch (Throwable var2) {
            throw new RuntimeSqlException("Could not set AutoCommit to " + this.autoCommit + ". Cause: " + var2, var2);
        }
    }

    private void commitConnection() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.commit();
            }

        } catch (Throwable var2) {
            throw new RuntimeSqlException("Could not commit transaction. Cause: " + var2, var2);
        }
    }

    private void rollbackConnection() {
        try {
            if (!this.connection.getAutoCommit()) {
                this.connection.rollback();
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    private void checkForMissingLineTerminator(StringBuilder command) {
        if (command != null && command.toString().trim().length() > 0) {
            throw new RuntimeSqlException("Line missing end-of-line terminator (" + this.delimiter + ") => " + command);
        }
    }

    private StringBuilder handleLine(StringBuilder command, String line) throws SQLException {
        String trimmedLine = line.trim();
        if (this.lineIsComment(trimmedLine)) {
            String cleanedString = trimmedLine.substring(2).trim().replaceFirst("//", "");
            if (cleanedString.toUpperCase().startsWith("@DELIMITER")) {
                this.delimiter = cleanedString.substring(11, 12);
                return command;
            }

            this.println(trimmedLine);
        } else if (this.commandReadyToExecute(trimmedLine)) {
            command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
            command.append(LINE_SEPARATOR);
            this.println(command);
            this.executeStatement(command.toString());
            command.setLength(0);
        } else if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(LINE_SEPARATOR);
        }

        return command;
    }

    private boolean lineIsComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
    }

    private boolean commandReadyToExecute(String trimmedLine) {
        return !this.fullLineDelimiter && trimmedLine.contains(this.delimiter) || this.fullLineDelimiter && trimmedLine.equals(this.delimiter);
    }

    private void executeStatement(String command) throws SQLException {
        boolean hasResults = false;
        Statement statement = this.connection.createStatement();
        statement.setEscapeProcessing(this.escapeProcessing);
        String sql = command;
        if (this.removeCrs) {
            sql = command.replaceAll("\r\n", "\n");
        }

        if (this.stopOnError) {
            hasResults = statement.execute(sql);
        } else {
            try {
                hasResults = statement.execute(sql);
            } catch (SQLException var8) {
                String message = "Error executing: " + command + ".  Cause: " + var8;
                this.printlnError(message);
            }
        }

        this.printResults(statement, hasResults);

        try {
            statement.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    private void printResults(Statement statement, boolean hasResults) {
        try {
            if (hasResults) {
                ResultSet rs = statement.getResultSet();
                if (rs != null) {
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();

                    int i;
                    String value;
                    for(i = 0; i < cols; ++i) {
                        value = md.getColumnLabel(i + 1);
                        this.print(value + "\t");
                    }

                    this.println("");

                    while(rs.next()) {
                        for(i = 0; i < cols; ++i) {
                            value = rs.getString(i + 1);
                            this.print(value + "\t");
                        }

                        this.println("");
                    }
                }
            }
        } catch (SQLException var8) {
            this.printlnError("Error printing results: " + var8.getMessage());
        }

    }

    private void print(Object o) {
        if (this.logWriter != null) {
            this.logWriter.print(o);
            this.logWriter.flush();
        }

    }

    private void println(Object o) {
        if (this.logWriter != null) {
            this.logWriter.println(o);
            this.logWriter.flush();
        }

    }

    private void printlnError(Object o) {
        if (this.errorLogWriter != null) {
            this.errorLogWriter.println(o);
            this.errorLogWriter.flush();
        }

    }
}
