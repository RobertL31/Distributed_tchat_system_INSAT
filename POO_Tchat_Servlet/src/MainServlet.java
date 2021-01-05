
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>\n"
					+ "<head><title>Title Hello World !</title></head>\n"
					+ "<body>\n"
					+ "<h1>Bonjour Monde !</h1>\n"
					+ "</body></html>");
	}

}
