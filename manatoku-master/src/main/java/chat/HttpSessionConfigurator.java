package chat;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.*;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {

        // 기존 HTTP 세션 꺼내기
        HttpSession httpSession = (HttpSession) request.getHttpSession();

        // WebSocket 설정에 저장
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}