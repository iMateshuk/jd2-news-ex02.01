package by.http.news.controller.impl;

import java.io.IOException;

import by.http.news.bean.News;
import by.http.news.controller.Command;
import by.http.news.controller.CommandName;
import by.http.news.service.NewsService;
import by.http.news.service.ServiceException;
import by.http.news.service.ServiceProvider;
import by.http.news.util.CheckSession;
import by.http.news.util.Creator;
import by.http.news.util.CreatorProvider;
import by.http.news.util.LogWriter;
import by.http.news.util.UtilException;
import by.http.news.util.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewsOperDelete implements Command {

	private static final NewsService newsServices = ServiceProvider.getInstance().getNewsService();

	private final static Creator<News, HttpServletRequest> CREATOR = CreatorProvider.getCreatorProvider()
			.getNewsDataCreator();

	private final static String commandAnswer = CommandName.NEWS_ANSWER.toString().toLowerCase();
	private final static String commandDelete = CommandName.NEWS_DELETE.toString().toLowerCase();
	private final static String commandAuth = CommandName.USER_AUTHORIZATION.toString().toLowerCase();

	final static String PATH = "/WEB-INF/jsp/" + commandAnswer + ".jsp";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {

			CheckSession.validate(request);

		} catch (UtilException e) {

			LogWriter.writeLog(e);
			response.sendRedirect("Controller?command=" + commandAuth + "&message=" + e.getMessage());
		}

		try {

			CheckSession.validateRoleUser(request);

			News news = CREATOR.create(request);

			View.print(news);

			newsServices.delete(news);

			response.sendRedirect("Controller?command=" + commandAnswer + "&action=" + commandDelete);

		} catch (ServiceException | UtilException e) {
			// TODO: handle exception

			LogWriter.writeLog(e);

			response.sendRedirect(
					"Controller?command=" + commandAnswer + "&message=" + e.getMessage() + "&action=" + commandDelete);
		}

	}

}
