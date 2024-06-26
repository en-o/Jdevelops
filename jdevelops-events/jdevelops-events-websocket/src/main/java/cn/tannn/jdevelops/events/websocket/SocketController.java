package cn.tannn.jdevelops.events.websocket;

import cn.tannn.jdevelops.events.websocket.core.WebSocketServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * websocket 基础接口
 *
 * @author tn
 * @date 2020-07-08 12:40
 */
@RestController
public class SocketController {

    public final WebSocketServer webSocketServer;

    public SocketController(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    /**
     * 给指定用户推送消息
     *
     * @param userName 用户名
     * @param message  消息
     */
    @GetMapping(value = "/only")
    @Operation(summary = "给指定用户推送消息")
    @Parameter(name = "userName", description = "用户名", required = true)
    @Parameter(name = "message", description = "消息", required = true)
    public Object onlyUserSocket(@RequestParam("userName") String userName, @RequestParam("message") String message) {
        webSocketServer.sendInfo(userName, message);
        return "success";
    }


    /**
     * 给指定用户推送消息 (异步)
     *
     * @param userName 用户名
     * @param message  消息
     */
    @GetMapping(value = "/only/async")
    @Operation(summary = "给指定用户推送消息(异步)")
    @Parameter(name = "userName", description = "用户名", required = true)
    @Parameter(name = "message", description = "消息", required = true)
    public Object onlyUserAsyncSocket(@RequestParam("userName") String userName, @RequestParam("message") String message) {
        webSocketServer.sendAsyncInfo(userName, message);
        return "success";
    }

    /**
     * 给所有用户推送消息
     *
     * @param message 消息
     */
    @Operation(summary = "给所有用户推送消息")
    @Parameter(name = "message", description = "消息", required = true)
    @GetMapping(value = "/all")
    public Object allUserSocket(@RequestParam("message") String message) {
        webSocketServer.onMessage(message);
        return "success";
    }



    /**
     * 给所有用户推送消息(异步)
     *
     * @param message 消息
     */
    @Operation(summary = "给所有用户推送消息(异步)")
    @Parameter(name = "message", description = "消息", required = true)
    @GetMapping(value = "/all/async")
    public Object allUserAsyncSocket(@RequestParam("message") String message) {
        webSocketServer.onAsyncMessage(message);
        return "success";
    }


    /**
     * 使用 sendInfoByLikeKey 进行模糊匹配用户进行消息发送
     * 匹配  keyPrefix 开头的 websocket 用户 给他们发送消息 （keyPrefix用户1, keyPrefix用户2）
     *
     * @param keyPrefix 主键前缀
     * @param message   消息
     */
    @Operation(summary = "模糊匹配用户进行消息发送")
    @Parameter(name = "keyPrefix", description = "用户关键字", required = true)
    @Parameter(name = "message", description = "消息", required = true)
    @GetMapping("/prefix")
    public Object sendInfoByLikeKey(@RequestParam("keyPrefix") String keyPrefix, @RequestParam("message") String message) {
        webSocketServer.sendInfoByLikeKey(keyPrefix, message);
        return "success";
    }

    /**
     * (异步)
     *
     * 使用 sendInfoByLikeKey 进行模糊匹配用户进行消息发送
     * 匹配  keyPrefix 开头的 websocket 用户 给他们发送消息 （keyPrefix用户1, keyPrefix用户2）
     *
     * @param keyPrefix 主键前缀
     * @param message   消息
     */
    @Operation(summary = "模糊匹配用户进行消息发送(异步)")
    @Parameter(name = "keyPrefix", description = "用户关键字", required = true)
    @Parameter(name = "message", description = "消息", required = true)
    @GetMapping("/prefix/async")
    public Object sendInfoAsyncByLikeKey(@RequestParam("keyPrefix") String keyPrefix, @RequestParam("message") String message) {
        webSocketServer.sendAsyncInfoByLikeKey(keyPrefix, message);
        return "success";
    }

}
