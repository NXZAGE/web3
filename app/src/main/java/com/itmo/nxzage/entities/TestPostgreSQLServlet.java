package com.itmo.nxzage.entities;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/test-pg")
public class TestPostgreSQLServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        out.println("<html><head><title>PostgreSQL Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".error { color: red; }");
        out.println(".warning { color: orange; }");
        out.println("table { border-collapse: collapse; margin: 10px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h1>PostgreSQL Connection Test</h1>");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            // 1. Создаем properties для подключения
            Properties props = new Properties();
            props.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
            props.put("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/studs");
            props.put("jakarta.persistence.jdbc.user", "s466828");
            props.put("jakarta.persistence.jdbc.password", "S420iSC5emrFnJ1Q");
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.show_sql", "true");
            
            // 2. Создаем EntityManagerFactory
            emf = Persistence.createEntityManagerFactory("default", props);
            out.println("<p class='success'>✓ EntityManagerFactory created</p>");
            
            // 3. Проверяем провайдера
            Map<String, Object> properties = emf.getProperties();
            String provider = (String) properties.get("jakarta.persistence.provider");
            out.println("<p>JPA Provider: " + (provider != null ? provider : "Hibernate") + "</p>");
            
            // 4. Создаем EntityManager
            em = emf.createEntityManager();
            out.println("<p class='success'>✓ EntityManager created</p>");
            
            // 5. Тестируем подключение к PostgreSQL
            out.println("<h2>Testing PostgreSQL connection:</h2>");
            
            // Простой запрос
            Object result = em.createNativeQuery("SELECT 1").getSingleResult();
            out.println("<p>SELECT 1 = " + result + " <span class='success'>✓</span></p>");
            
            // Версия PostgreSQL
            String version = (String) em.createNativeQuery("SELECT version()").getSingleResult();
            out.println("<p>PostgreSQL Version: " + version + " <span class='success'>✓</span></p>");
            
            // Проверяем, что это действительно PostgreSQL
            if (version.contains("PostgreSQL")) {
                out.println("<h3 class='success'>✅ SUCCESS! Connected to PostgreSQL!</h3>");
            } else {
                out.println("<h3 class='error'>❌ NOT connected to PostgreSQL!</h3>");
            }
            
            // 6. Список баз данных - ИСПРАВЛЕННАЯ ВЕРСИЯ
            out.println("<h3>Database Information:</h3>");
            
            try {
                // Запрос возвращает строки, а не массивы
                List<String> dbs = em.createNativeQuery(
                    "SELECT datname FROM pg_database WHERE datistemplate = false ORDER BY datname"
                ).getResultList();
                
                out.println("<p>Total databases: " + dbs.size() + "</p>");
                out.println("<table>");
                out.println("<tr><th>Database Name</th></tr>");
                for (String db : dbs) {
                    out.println("<tr><td>" + db + "</td></tr>");
                }
                out.println("</table>");
                
                // Проверяем текущую базу данных
                String currentDb = (String) em.createNativeQuery(
                    "SELECT current_database()"
                ).getSingleResult();
                out.println("<p>Current database: <strong>" + currentDb + "</strong></p>");
                
            } catch (Exception e) {
                out.println("<p class='warning'>⚠ Could not list databases: " + e.getMessage() + "</p>");
            }
            
            // 7. Проверяем схему s466828
            out.println("<h3>Checking schema 's466828':</h3>");
            try {
                // ИСПРАВЛЕННЫЙ ЗАПРОС
                List<String> schemas = em.createNativeQuery(
                    "SELECT schema_name FROM information_schema.schemata " +
                    "WHERE schema_name = 's466828'"
                ).getResultList();
                
                if (schemas.isEmpty()) {
                    out.println("<p class='warning'>Schema 's466828' does not exist</p>");
                    out.println("<p>Creating schema...</p>");
                    em.getTransaction().begin();
                    em.createNativeQuery("CREATE SCHEMA IF NOT EXISTS s466828").executeUpdate();
                    em.getTransaction().commit();
                    out.println("<p class='success'>✓ Schema 's466828' created successfully</p>");
                } else {
                    out.println("<p class='success'>✓ Schema 's466828' exists</p>");
                    
                    // Показываем таблицы в схеме
                    List<String> tables = em.createNativeQuery(
                        "SELECT table_name FROM information_schema.tables " +
                        "WHERE table_schema = 's466828' ORDER BY table_name"
                    ).getResultList();
                    
                    out.println("<p>Tables in schema 's466828': " + tables.size() + "</p>");
                    if (!tables.isEmpty()) {
                        out.println("<table>");
                        out.println("<tr><th>Table Name</th></tr>");
                        for (String table : tables) {
                            out.println("<tr><td>" + table + "</td></tr>");
                        }
                        out.println("</table>");
                    }
                }
            } catch (Exception e) {
                out.println("<p class='warning'>Error checking schema: " + e.getMessage() + "</p>");
            }
            
            // 8. Тестируем создание таблицы User
            out.println("<h3>Testing User table creation:</h3>");
            try {
                em.getTransaction().begin();
                
                // Создаем таблицу с правильным именем (в нижнем регистре)
                em.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS s466828.users (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL UNIQUE, " +
                    "email VARCHAR(255) NOT NULL" +
                    ")"
                ).executeUpdate();
                
                // Проверяем, существует ли таблица
                Long tableCount = (Long) em.createNativeQuery(
                    "SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = 's466828' AND table_name = 'users'"
                ).getSingleResult();
                
                em.getTransaction().commit();
                
                if (tableCount > 0) {
                    out.println("<p class='success'>✓ Table 'users' created/verified in schema 's466828'</p>");
                    
                    // Вставляем тестовые данные
                    em.getTransaction().begin();
                    em.createNativeQuery(
                        "INSERT INTO s466828.users (username, email) " +
                        "VALUES ('test_user', 'test@example.com') " +
                        "ON CONFLICT (username) DO NOTHING"
                    ).executeUpdate();
                    em.getTransaction().commit();
                    
                    // Считаем записи
                    Long rowCount = (Long) em.createNativeQuery(
                        "SELECT COUNT(*) FROM s466828.users"
                    ).getSingleResult();
                    out.println("<p>Rows in users table: " + rowCount + "</p>");
                }
                
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                out.println("<p class='warning'>Table creation test: " + e.getMessage() + "</p>");
            }
            
            // 9. Информация о Hibernate
            out.println("<h3>Hibernate Information:</h3>");
            try {
                String hibernateVersion = (String) emf.getProperties().get("hibernate.version");
                out.println("<p>Hibernate Version: " + hibernateVersion + "</p>");
                
                // Проверяем диалект
                String dialect = (String) emf.getProperties().get("hibernate.dialect");
                out.println("<p>Dialect: " + dialect + "</p>");
                
            } catch (Exception e) {
                out.println("<p class='warning'>Hibernate info not available</p>");
            }
            
            out.println("<hr>");
            out.println("<h2 class='success'>🎉 PostgreSQL Connection Test COMPLETE!</h2>");
            out.println("<p>Your application is now connected to PostgreSQL at localhost:5432/studs</p>");
            
        } catch (Exception e) {
            out.println("<h2 class='error'>❌ ERROR: " + e.getMessage() + "</h2>");
            
            // Дополнительная диагностика
            out.println("<h3>Diagnostics:</h3>");
            out.println("<p>Trying to load PostgreSQL driver directly...</p>");
            try {
                Class.forName("org.postgresql.Driver");
                out.println("<p class='success'>✓ PostgreSQL driver found in classpath</p>");
            } catch (ClassNotFoundException cnfe) {
                out.println("<p class='error'>❌ PostgreSQL driver NOT found in classpath</p>");
            }
            
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
                out.println("<p>EntityManager closed</p>");
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
                out.println("<p>EntityManagerFactory closed</p>");
            }
        }
        
        // Ссылка для теста JPA Entity
        out.println("<hr>");
        out.println("<h3>Next Steps:</h3>");
        out.println("<ul>");
        out.println("<li><a href='/web3/test-entity'>Test JPA Entity Operations</a></li>");
        out.println("<li><a href='/web3'>Go to Application Home</a></li>");
        out.println("</ul>");
        
        out.println("</body></html>");
    }
}