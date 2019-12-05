package com.lab.listener;

import java.io.IOException;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lab.controller.UserAuthenticationController;
import com.lab.model.User;
import com.lab.util.ModelUtils;

public class LoginListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private UserAuthenticationController userAuthenticationController;

	private String[] paginasPublicas = { "pages/login", "pages/notfound" };

	public void afterPhase(PhaseEvent event) {

		userAuthenticationController = event.getFacesContext().getApplication().evaluateExpressionGet(
				event.getFacesContext(), "#{userAuthenticationController}", UserAuthenticationController.class);
		User user = userAuthenticationController != null ? userAuthenticationController.getAuthenticatedUser()
				: new User();
		boolean resource = false;
		String page = getPaginaAtual(event.getFacesContext(), resource);

		if (page.contains("javax.faces.resource") || page.contains(".css") || page.contains(".js")) {
			resource = true;
		}

		if (!resource) {

			if (ModelUtils.isNullEmpty(page))
				page = "/pages/login";

			if (!allowedPage(page)) {

				if (!user.isAuthenticated()) {
					navigate("/pages/login", event);
				} else {
					if (!user.hasPermission(page)) {
						navigate("/pages/access", event);
					}
				}
			} else if (page.contains("login") && user.isAuthenticated()) {
				navigate("/pages/home", event);
			}
		}
	}

	public void beforePhase(PhaseEvent event) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void navigate(String page, PhaseEvent event) {
		try {
			NavigationHandler nh = event.getFacesContext().getApplication().getNavigationHandler();
			nh.handleNavigation(event.getFacesContext(), null, page + "?faces-redirect=true");
		} catch (Exception e) {
		}

	}

	/**
	 * Verify if a page is public allowed
	 * 
	 * @param currentPage
	 * @return boolean
	 */
	private boolean allowedPage(String currentPage) {
		for (String s : paginasPublicas)
			if (currentPage.contains(s))
				return true;

		return false;
	}

	/**
	 * Redirect to page
	 * 
	 * @param page
	 * @param req
	 * @param resp
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void navegarParaPagina(String page, ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		response.sendRedirect(request.getContextPath() + "/" + page);
		resp.flushBuffer();
	}

	/**
	 * Forward page
	 * 
	 * @param page
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void forward(String page, ServletRequest req, ServletResponse resp)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		request.getRequestDispatcher(page).forward(req, resp);
	}

	/**
	 * Return current page from context
	 * 
	 * @param req
	 * @param resource
	 * @return String
	 */
	private String getPaginaAtual(FacesContext facesContext, Boolean resource) {
		String currentPage = facesContext.getViewRoot().getViewId();
		try {
			currentPage = currentPage.replace(facesContext.getExternalContext().getRequestContextPath() + "/", "");
			currentPage = currentPage.replace(".jsf", "").replace(".xhtml", "");
		} catch (Exception e) {
		}
		return currentPage;
	}

}