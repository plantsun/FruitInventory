package com.example.software.user;

import com.example.software.InformationBuffer;
import com.example.software.database.serves.Business;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class DeleteUserRequest {
    private int userId;
    private String operator;

    DeleteUserRequest(int userId, String operator)
    {
        this.userId = userId;
        this.operator = operator;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public String getOperator()
    {
        return this.operator;
    }
}

class DeleteUserResponse {
    private String flag;

    DeleteUserResponse() {}

    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}

@RestController
public class DeleteUserController {
    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest)
    {
        int usrId = deleteUserRequest.getUserId();
        String operator = deleteUserRequest.getOperator();
        System.out.println("[DeleteUser]id: " + usrId);
        System.out.println("[DeleteUser]operator: " + operator);

        DeleteUserResponse response = new DeleteUserResponse();
        Business business = InformationBuffer.getBusiness(operator);
        if (business != null)
        {
            if (business.getUser().getUserId() == usrId)    // 假如用户注销自己
            {
                String res = business.deleteUser(usrId);
                if (res.equals("删除用户成功"))
                {
                    response.setFlag("1");
                    InformationBuffer.deleteBusiness(business);
                }
                else
                {
                    System.out.println("[DeleteUser][User]: " + res);
                    response.setFlag("0");
                }
            }
            else
            {
                if (business.checkAdmin())  // 管理才能注销别的用户
                {
                    String res = business.deleteUser(usrId);
                    if (res.equals("删除用户成功"))
                    {
                        response.setFlag("1");
                        Business business1 = InformationBuffer.getBusiness(usrId);
                        if (business1 != null)  // 如果用户已经登录
                        {
                            InformationBuffer.deleteBusiness(business1);
                        }
                    }
                    else
                    {
                        System.out.println("[DeleteUser][Admin]: " + res);
                        response.setFlag("0");
                    }
                }
                else
                {
                    System.out.println("[DeleteUser]没有权限");
                    response.setFlag("0");
                }
            }
        }
        else
        {
            System.out.println("[DeleteUser]用户未登录");
            response.setFlag("0");
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(response);
        System.out.println("[DeleteUser]Response: " + jsonResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
    }
}
