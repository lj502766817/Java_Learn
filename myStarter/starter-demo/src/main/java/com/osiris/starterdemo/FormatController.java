package com.osiris.starterdemo;

import com.osiris.MyFormatTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijia at 2020-07-14
 */
@RestController
public class FormatController {

    @Autowired
    private MyFormatTemplate myFormatTemplate;

    @GetMapping("/template")
    public String template(){
        User user = new User();
        user.setName("lijia");
        user.setAge(18);
        return myFormatTemplate.doFormat(user);
    }

}
