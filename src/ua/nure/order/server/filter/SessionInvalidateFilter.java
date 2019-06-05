package ua.nure.order.server.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Forwards to login page when the current session is invalidate.
 */
public class SessionInvalidateFilter implements Filter {

	private static final Logger log = Logger
			.getLogger(SessionInvalidateFilter.class);

	public void destroy() {
		// do nothing
	}

	/**
	 * Forwards to login page when the current session is invalidate.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		log.debug("Filter starts");

		// request is the HTTP request at this point
		// cast it to HttpServletRequest to get possibility to obtain the HTTP session
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		// if there is no a valid session
		if (httpServletRequest.getSession(false) == null) {
			// create the new session
			httpServletRequest.getSession(true);

			// and forward to the login page
			httpServletRequest.getRequestDispatcher("/login.jsp").forward(
					request, response);
			return;
		}

		// otherwise go further
		chain.doFilter(request, response);
		log.debug("Filter finished");
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// do nothing
	}

}
