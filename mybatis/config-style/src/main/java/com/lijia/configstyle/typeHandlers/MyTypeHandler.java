package com.lijia.configstyle.typeHandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : 李佳
 * @Description : typeHandler Demo
 * @Date : 2019/3/28 0028
 * @Email : 502766817@qq.com
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MyTypeHandler extends BaseTypeHandler<String> {

    public MyTypeHandler(){}

    /**用作插入 */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,o+"学生");
    }

    /**用作返回的结果集 */
    @Override
    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getString(s).replace("学生","stu");
    }

    /**用作返回的结果集 */
    @Override
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString(i).replace("学生","stu");
    }

    /**用作返回的结果集 */
    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getString(i).replace("学生","stu");
    }
}
