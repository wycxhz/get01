layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 监听表单submit事件
     form.on('submit(按钮元素的lay-filter属性值)', function (data) {

        });
     */
    form.on('submit(addOrUpdateSaleChance)', function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...", {
            icon: 16, // 图标
            time: false, // 不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        // 发送ajax请求
        var url = ctx + "/sale_chance/add"; // 添加操作


        // 通过营销机会的ID来判断当前需要执行添加操作还是修改操作
        // 如果营销机会的ID为空，则表示执行添加操作；如果ID不为空，则表示执行更新操作
        // 通过获取隐藏域中的ID
        var saleChanceId = $("[name='id']").val();
        // 判断ID是否为空
        if (saleChanceId != null && saleChanceId != '') {
            // 更新操作
            url = ctx + "/sale_chance/update";
        }

        $.post(url, data.field, function (result) {
            // 判断操作是否执行成功 200=成功
            if (result.code == 200) {
                // 成功
                // 提示成功
                layer.msg("操作成功！", {icon: 6});
                // 关闭加载层
                layer.close(index);
                // 关闭弹出层
                layer.closeAll("iframe");
                // 刷新父窗口，重新加载数据
                parent.location.reload();
            } else {
                // 失败
                layer.msg(result.msg, {icon: 5});
            }
        });

        // 阻止表单提交
        return false;

    });


    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        // 先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        // 再执⾏关闭
        parent.layer.close(index);
    });


    $.post(ctx + "/user/queryAllSales", function (data) {
        // 如果是修改操作，判断当前修改记录的指派⼈的值
        var assignMan = $("#assignManId").val();
        for (var i = 0; i < data.length; i++) {
            // 当前修改记录的指派⼈的值 与 循环到的值 相等，下拉框则选中
            if (assignMan == data[i].id) {
                $("#assignMan").append('<option value="'+ data[i].id+'"selected >'+data[i].uname+'</option>');
            } else {
                $("#assignMan").append('<option value = "' + data[i].id + '">'+data[i].uname+'</option>');
            }
            layui.form.render("select");
        }


    });
});