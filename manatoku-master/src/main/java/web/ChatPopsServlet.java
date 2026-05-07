package web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/getOgData")
public class ChatPopsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter("url");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/110.0.0.0")//doc가 크롤링할때 봇인걸 감지 못하게 크롬브라우저라고 인식하게 하는것
					/* .timeout(5000) */
                    .get();

            String title = getMeta(doc, "og:title");
            if(title.isEmpty()) title = doc.title();
            
            String desc = getMeta(doc, "og:description");
            String image = getMeta(doc, "og:image");

            // 유튜브 특별 처리
            if (url.contains("youtube.com") || url.contains("youtu.be")) {
                String vId = "";
                if(url.contains("v=")) vId = url.split("v=")[1].split("&")[0];
                else if(url.contains("youtu.be/")) vId = url.split("youtu.be/")[1].split("\\?")[0];
                if(!vId.isEmpty()) image = "https://img.youtube.com/vi/" + vId + "/mqdefault.jpg";
            }

            // JSON 형식을 직접 문자열로 완벽하게 조립
            String jsonResponse = "{"
                + "\"title\":\"" + clean(title) + "\","
                + "\"desc\":\"" + clean(desc) + "\","
                + "\"image\":\"" + clean(image) + "\""
                + "}";
            
            response.getWriter().write(jsonResponse);
            
        } catch (Exception e) {
            response.getWriter().write("{\"error\":\"fail\"}");
        }
    }

    private String getMeta(Document doc, String attr) {
        Element tag = doc.select("meta[property=" + attr + "]").first();
        if(tag == null) tag = doc.select("meta[name=" + attr + "]").first();
        return (tag != null) ? tag.attr("content") : "";
    }

    private String clean(String str) {
        if(str == null) return "";
        return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
    }
}