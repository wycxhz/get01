layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 表单的submit监听
     *     form.on('submit(按钮元素的lay-filter属性值)',function (data){
     *
     *     }); */
    form.on('submit(saveBtn)',function (data){
        //所有表单元素的值
        var fieldData = data.field;

        //发送Ajax请求，修改用户密码
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePwd",
            data:{
                oldPassword:fieldData.old_password,
                newPassword:fieldData.new_password,
                confirmPassword:fieldData.again_password,

            },
            dataType:"json",
            success:function(data){
                //判断是否成功
                if(data.code == 200){
                    //修改成功后，用户自动退出系统
                    layer.msg("用户密码修改成功，系统将在3秒钟后退出...",function (){
                        //退出系统后，删除对应的cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //跳转到登录页面
                        window.parent.location.href = ctx + "/index";
                    });

                }else{
                    layer.msg(data.msg);
                }
            }
        });
    });

});