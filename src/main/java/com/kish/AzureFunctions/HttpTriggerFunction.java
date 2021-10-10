package com.kish.AzureFunctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;


/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerFunction {
    @FunctionName("api-java-employee-insert")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) 
            HttpRequestMessage<Optional<Employee>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processou uma solicitação");

        Employee emp = request.getBody().get();

        context.getLogger().info("emp id."+emp.getId());
        context.getLogger().info("emp Name."+emp.getEname());
        
        try
        {
        	     // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        	     String url ="jdbc:sqlserver://sqlserver-rm9999.database.windows.net:1433;database=bdemployee;user=admsql@sqlserver-rm9999;password=Function@fiap21;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                 Connection connect = DriverManager.getConnection(url);
                 String sql="INSERT INTO dbo.employee(id,ename,email,ssn) Values (?,?,?,?)";
                 PreparedStatement statement = connect.prepareStatement(sql);
                 statement.setInt(1, emp.getId());
                 statement.setString(2, emp.getEname());
                 statement.setString(3, emp.getEmail());
                 statement.setString(4, emp.getSsn());
                 statement.execute();
                 }
                 catch(Exception e)
                 {
                 	return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Exception "+e.getMessage()).build();
                 }
            return request.createResponseBuilder(HttpStatus.OK).body("Olá , "+emp.getId() +" "+ emp.getEname()).build();
    }

}
