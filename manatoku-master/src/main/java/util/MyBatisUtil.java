package util;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {
    private static final SqlSessionFactory factory;

    static {
        try (InputStream in = Resources.getResourceAsStream("mybatis-config.xml")) {
            factory = new SqlSessionFactoryBuilder().build(in);
        } catch (Exception e) {
            throw new RuntimeException("MyBatis 초기화 실패", e);
        }
    }

    public static SqlSessionFactory getFactory() {
        return factory;
    }
}