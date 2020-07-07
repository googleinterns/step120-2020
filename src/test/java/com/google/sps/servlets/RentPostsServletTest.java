package com.google.sps;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class RentPostsServletTest {
 
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;
 
  private RentPostsServlet rentPostsServlet;
  private StringWriter stringWriter;
  private final LocalServiceTestHelper datastoreConfiguration =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
 
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    rentPostsServlet = new RentPostsServlet();
    rentPostsServlet.init();
    stringWriter = new StringWriter();
  }
}