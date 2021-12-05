package cn.jdevelop.console;

/**
 * 控制台输出颜色
 *  <code>System.out.println(ConsoleColors.RED + "RED COLORED"  + ConsoleColors.RESET + " NORMAL");</code>
 */
public class ConsoleColors {
    // Reset
    public static final String RESET = "33[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "33[0;30m";   // BLACK
    public static final String RED = "33[0;31m";     // RED
    public static final String GREEN = "33[0;32m";   // GREEN
    public static final String YELLOW = "33[0;33m";  // YELLOW
    public static final String BLUE = "33[0;34m";    // BLUE
    public static final String PURPLE = "33[0;35m";  // PURPLE
    public static final String CYAN = "33[0;36m";    // CYAN
    public static final String WHITE = "33[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "33[1;30m";  // BLACK
    public static final String RED_BOLD = "33[1;31m";    // RED
    public static final String GREEN_BOLD = "33[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "33[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "33[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "33[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "33[1;36m";   // CYAN
    public static final String WHITE_BOLD = "33[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "33[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "33[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "33[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "33[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "33[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "33[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "33[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "33[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "33[40m";  // BLACK
    public static final String RED_BACKGROUND = "33[41m";    // RED
    public static final String GREEN_BACKGROUND = "33[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "33[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "33[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "33[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "33[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "33[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "33[0;90m";  // BLACK
    public static final String RED_BRIGHT = "33[0;91m";    // RED
    public static final String GREEN_BRIGHT = "33[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "33[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "33[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "33[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "33[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "33[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "33[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "33[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "33[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "33[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "33[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "33[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "33[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "33[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "33[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "33[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "33[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "33[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "33[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "33[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "33[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "33[0;107m";   // WHITE
}