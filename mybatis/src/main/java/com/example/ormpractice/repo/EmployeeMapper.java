package com.example.ormpractice.repo;


import com.example.ormpractice.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("delete from employee")
    void deleteAll();

    @Select("select * from employee where name =  #{name}")
    @Results(id = "employeeMap", value = {
            @Result(property = "id", column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "byteDanceId", column = "byteDanceId", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "age", column = "age", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
    })
    List<Employee> findByName(String name);

    @Select("select * from employee")
    @ResultType(Employee.class)
    List<Employee> findAllWithResultType();

    @Select("select * from employee")
    @ResultMap("employeeMap")
    List<Employee> findAllWithResultMap();

    @Insert("insert into employee(byteDanceId, name, age) values(#{byteDanceId}, #{name}, #{age})")
    int insert(Employee employee);
}
