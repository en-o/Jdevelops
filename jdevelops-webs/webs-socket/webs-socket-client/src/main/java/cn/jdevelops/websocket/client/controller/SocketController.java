package cn.jdevelops.websocket.client.controller;

import cn.jdevelops.websocket.core.service.WebSocketServer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Object onlyUserSocket(@RequestParam("userName") String userName, @RequestParam("message") String message){
        webSocketServer.sendInfo(userName, message);
        return "success";
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
    public Object allUserSocket(@RequestParam("message") String message){
        webSocketServer.onMessage(message);
        return "success";
    }


    /**
     *  使用 sendInfoByLikeKey 进行模糊匹配用户进行消息发送
     *  匹配  keyPrefix 开头的 websocket 用户 给他们发送消息 （keyPrefix用户1, keyPrefix用户2）
     *  @param keyPrefix 主键前缀
     *  @param message 消息
     */
    @ApiOperation("模糊匹配用户进行消息发送")
    @ApiImplicitParams({
            @ApiImplicitParam(name="keyPrefix", value="用户关键字", dataType="String", required=true),
            @ApiImplicitParam(name="message", value="消息", dataType="String", required=true)
    })
    @RequestMapping("/sendInfoByLikeKey")
    public Object sendInfoByLikeKey(@RequestParam("keyPrefix") String keyPrefix, @RequestParam("message") String message){
        webSocketServer.sendInfoByLikeKey(keyPrefix,message);
        return "success";
    }

}
