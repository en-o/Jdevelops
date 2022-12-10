package cn.jdevelops.websocket.client.controller;

import cn.jdevelops.websocket.core.service.WebSocketServer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * websocket 基础接口
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
     * @param userName 用户名
     * @param message 消息
     */
    @GetMapping(value = "/only")
    @ApiOperation("给指定用户推送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName", value="用户名",  dataType="String", required=true),
            @ApiImplicitParam(name="message", value="消息", dataType="String", required=true)
    })
    public void onlyUserSocket(@RequestParam("userName") String userName, @RequestParam("message") String message){
        webSocketServer.sendInfo(userName, message);
    }

    /**
     * 给所有用户推送消息
     * @param message 消息
     */
    @ApiOperation("给所有用户推送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="message", value="消息", dataType="String", required=true)
    })
    @GetMapping(value = "/all")
    public void allUserSocket(@RequestParam("message") String message){
        webSocketServer.onMessage(message);
    }
}
