package com.google.roomies;

import static com.google.roomies.ProjectConstants.INDEX_URL;
import static com.google.roomies.CommentConstants.COMMENT_COLLECTION_NAME;

import com.google.cloud.Timestamp;

import com.google.roomies.database.NoSQLDatabase;
import com.google.roomies.database.DatabaseFactory;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet that posts comments. */
@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {
  private NoSQLDatabase database;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      database = DatabaseFactory.getDatabase();
      System.out.println("get database");

      Comment comment = Comment.fromServletRequest(request);
      System.out.println("got comment");
      database.addCommentAsMap(COMMENT_COLLECTION_NAME, comment);
  // City city = new City(
  // "hSwk2Inz63kweyFJdUiJ", "test comment");
  // database.addCityAsClass("CITIES", city);
  //     System.out.println("add document to class");

      response.sendRedirect(INDEX_URL);
    } catch (Exception e) {
        System.err.println("Error posting comment " + e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

}