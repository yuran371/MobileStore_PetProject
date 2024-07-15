package filter;

//@WebFilter(value = "/*")
//public class AuthorizationFilter extends HttpFilter{
//	@Override
//	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
//			throws IOException, ServletException {
//		String requestURI = req.getRequestURI();
//		HttpSession session = req.getSession();
//		if(privateURI(requestURI) && notAuthorized(session)) {
//			res.sendRedirect(LOGIN);
//		} else {
//			chain.doFilter(req, res);
//		}
//	}
//
//	private boolean privateURI(String requestURI) {
//		 Set<String> set = Set.of(CART, LOGOUT);
//		 return set.stream().anyMatch(requestURI::startsWith);
//	}
//
//	private boolean notAuthorized(HttpSession session) {
//		return session.getAttribute(LoginServlet.USER) == null;
//	}
//
//}
