package com.ceej.controller;

import com.ceej.model.User;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataBaseUtilityTest {

    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    DataBaseUtility dao;

    class DataBaseUtilFake extends DataBaseUtility {
        @Override
        protected Connection createConnection() {
            return connection;
        }
    }

    @BeforeEach
    void setUp() {
        dao = new DataBaseUtilFake();
    }

    @Test
    void is_user_exist() throws Exception {
        String userId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        boolean success = dao.isUserExisted(userId);
        assertFalse(success);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement).setString(eq(1), stringArgumentCaptor.capture());

        assertEquals("1", stringArgumentCaptor.getAllValues().get(0));

//        verify(statement).executeUpdate();
//        verify(connection).commit();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void add_user() throws SQLException {
        User user = new User();
        user.setNickname("test1");
        user.setPassword("123456");
        user.setUserID("42");

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        boolean success = dao.addUser(user.getUserID(), user.getNickname(), user.getPassword());
        assertTrue(success);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(statement, times(4)).setString(integerArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals("42",stringArgumentCaptor.getAllValues().get(1));
        assertEquals("test1",stringArgumentCaptor.getAllValues().get(2));
        assertEquals("123456",stringArgumentCaptor.getAllValues().get(3));

        verify(statement).executeUpdate();
//        这个可以不调用吗 不提交的话好像是不会修改数据库的吧
        verify(connection).commit();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void is_pwd_correct() throws SQLException {
        String userId = "1";
        String password = "123456";
        when(connection.prepareStatement(anyString())).thenReturn(statement);
//        when(statement.executeQuery()).thenReturn(new ResultSetImpl());
//        这里要怎么才能让这个返回值为true啊
//        如果不连数据库的话 必定是false的 调用后返回的resultSet为null
        boolean success = dao.isPwdCorrect(userId, password);
        assertFalse(success);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement).setString(eq(1), stringArgumentCaptor.capture());

        assertEquals("1", stringArgumentCaptor.getAllValues().get(0));

        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void get_nickname() throws SQLException {
        String userId = "1";
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        String result = dao.getNickname(userId);
        assertNull(result);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement).setString(eq(1), stringArgumentCaptor.capture());

        assertEquals("1", stringArgumentCaptor.getAllValues().get(0));

        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void is_article_existed() throws SQLException {
        String articleId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        boolean success = dao.isArticleExisted(articleId);
        assertFalse(success);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement).setString(eq(1), stringArgumentCaptor.capture());

        assertEquals("1", stringArgumentCaptor.getAllValues().get(0));

        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void add_article() throws SQLException {
        String userId = "1";
        String content = "this is an article for testing.";
        String imageUrl = "res/test1.jpg";

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        boolean success = dao.addArticle(userId,content,imageUrl);
        assertTrue(success);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(statement, times(4)).setString(integerArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals("1",stringArgumentCaptor.getAllValues().get(1));
        assertEquals("this is an article for testing.",stringArgumentCaptor.getAllValues().get(2));
        assertEquals("res/test1.jpg",stringArgumentCaptor.getAllValues().get(3));

        verify(statement).executeUpdate();
//        这个可以不调用吗 不提交的话好像是不会修改数据库的吧
        verify(connection).commit();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void get_current_articles() throws SQLException {
        int front = 12;
        int num = 3;
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        assertNotNull(dao.getCurrentArticles(front,num));

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(statement).setInt(eq(12), integerArgumentCaptor.capture());

        assertEquals(12, integerArgumentCaptor.getAllValues().get(0));

        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void get_previous_articles() throws SQLException {
        int tail = 12;
        int num = 3;
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        assertNotNull(dao.getCurrentArticles(tail,num));

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(statement).setInt(eq(12), integerArgumentCaptor.capture());

        assertEquals(12, integerArgumentCaptor.getAllValues().get(0));

        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void delete_article() throws SQLException {
        String articleId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        boolean success = dao.deleteArticle(articleId);
        assertTrue(success);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement, times(2)).setString(eq(1), stringArgumentCaptor.capture());

        assertEquals("1",stringArgumentCaptor.getAllValues().get(0));
        assertEquals("1",stringArgumentCaptor.getAllValues().get(1));

        verify(statement).executeUpdate();
//        这个可以不调用吗 不提交的话好像是不会修改数据库的吧
        verify(connection).commit();
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void get_image_url_of_article() throws SQLException {
        String articleId = "1";
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        String result = dao.getImageUrlOfArticle(articleId);
        assertNull(result);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(statement,times(2)).setString(eq(1), stringArgumentCaptor.capture());

        assertEquals("1", stringArgumentCaptor.getAllValues().get(0));
        assertEquals("1", stringArgumentCaptor.getAllValues().get(1));

        /*
        * org.mockito.exceptions.verification.TooManyActualInvocations:
preparedStatement.close();
Wanted 1 time:
-> at com.ceej.controller.DataBaseUtilityTest.get_image_url_of_article(DataBaseUtilityTest.java:242)
But was 2 times:
-> at com.ceej.controller.DataBaseUtility.closeConnection(DataBaseUtility.java:32)
-> at com.ceej.controller.DataBaseUtility.closeConnection(DataBaseUtility.java:32)
        * */
        verify(statement).close();
        verify(connection).close();
    }
}