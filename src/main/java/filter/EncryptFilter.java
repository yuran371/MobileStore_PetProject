package filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.crypto.dsig.spec.XPathType.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EncryptFilter extends HttpFilter{
	 @Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		 req.setCharacterEncoding(StandardCharsets.UTF_8.name());
		 res.setCharacterEncoding(StandardCharsets.UTF_8.name());
		 chain.doFilter(req, res);
	}
}
