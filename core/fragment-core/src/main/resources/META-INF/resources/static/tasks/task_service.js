var classInfos = [];
$(function () {
    getServices();

    transSelect2();

    $('select[name="serviceName"]').on('change', function () {
        buildMethod($(this).val());
        buildTable($(this).val());
    });

    $('select[name="methodName"]').on('change', function () {
        var serviceName = $('select[name="serviceName"]').val();
        buildTable(serviceName, $(this).val());
        buildParams(serviceName, $(this).val())
    });
})

function getServices() {
    $.ajax({
        url: '/fragment?method=services',
        type: 'POST',
        dataType: 'json',
        success: function (data, textStatus, jqXHR) {
            var ele = $('select[name="serviceName"]');
            if (data && data.length > 0) {
                classInfos = data;
                ele.empty();
                var option = $("<option>").val('').text('-请选择-');
                ele.append(option);
                $.each(data, function (i, item) {
                    option = $("<option>").val(item.className).text(item.className);
                    ele.append(option);
                });
            }
        }
    });
}

function buildMethod(beanName) {
    var ele = $('select[name="methodName"]');
    ele.empty();
    if (beanName) {
        var option = $("<option>").val('').text('-请选择-');
        ele.append(option);
        ele.select2("val","");
        $.each(classInfos, function (i, item) {
            if (item.className == beanName) {
                var methodInfos = item.methodInfoList;
                if (methodInfos) {
                    $.each(methodInfos, function (j, subitem) {
                        option = $("<option>").val(subitem.methodName).text(subitem.methodName);
                        ele.append(option);
                    });
                }
            }
        });
        if(!ele.val()){
            buildParams(null, null);
        }
    }
}


function transSelect2() {
    /**
     *  参考: http://ivaynberg.github.io/select2/
     */
    $('select[name="appGroup"]').select2({
        minimumInputLength: 0,
        dropdownCssClass: 'widthMin200'
    });
    $('select[name="serGroup"]').select2({
        minimumInputLength: 0,
        dropdownCssClass: 'widthMin200'
    });
    $('select[name="serviceName"]').select2({
        minimumInputLength: 0,
        dropdownCssClass: 'widthMin200'
    });
    $('select[name="methodName"]').select2({
        minimumInputLength: 0,
        dropdownCssClass: 'widthMin200'
    });
}

function buildTable(serviceName, methodName) {
    var tBody;
    var ele = $("tbody");
    ele.empty();
    if(methodName){
        $.each(classInfos, function (i, item) {
            if (item.className == serviceName) {
                var methodInfos = item.methodInfoList;
                if (methodInfos) {
                    $.each(methodInfos, function (j, subitem) {
                        if(subitem.methodName == methodName){
                            var tdName1 = $("<td></td>").html(j);
                            var tdName2 = $("<td></td>").html(serviceName);
                            var tdName3 = $("<td></td>").html(methodName);
                            var tdName4 = $("<td></td>").html(subitem.desc);
                            var tdName5 = $("<td></td>").html(item.appGroup);
                            var tdName6 = $("<td></td>").html(item.serGroup);
                            var tdName7 = $("<td></td>").html(item.desc);
                            tBody = $("<tr></tr>").append(tdName1)
                                .append(tdName2)
                                .append(tdName3)
                                .append(tdName4)
                                .append(tdName5)
                                .append(tdName6)
                                .append(tdName7);
                            ele.append(tBody);
                        }
                    });
                }
            }
        });
    }else if(serviceName){
        $.each(classInfos, function (i, item) {
            if (item.className == serviceName) {
                var methodInfos = item.methodInfoList;
                if (methodInfos) {
                    $.each(methodInfos, function (j, subitem) {
                        var tdName1 = $("<td></td>").html(j);
                        var tdName2 = $("<td></td>").html(serviceName);
                        var tdName3 = $("<td></td>").html(subitem.methodName);
                        var tdName4 = $("<td></td>").html(subitem.desc);
                        var tdName5 = $("<td></td>").html(item.appGroup);
                        var tdName6 = $("<td></td>").html(item.serGroup);
                        var tdName7 = $("<td></td>").html(item.desc);
                        tBody = $("<tr></tr>").append(tdName1)
                            .append(tdName2)
                            .append(tdName3)
                            .append(tdName4)
                            .append(tdName5)
                            .append(tdName6)
                            .append(tdName7);
                        ele.append(tBody);
                    });
                }
            }
        });
    }
}

function buildParams(serviceName, methodName) {
    $("#paramForm").empty();
    $.each(classInfos, function (i, item) {
        if (item.className == serviceName) {
            var methodInfos = item.methodInfoList;
            if (methodInfos) {
                $.each(methodInfos, function (j, subitem) {
                    if(subitem.methodName == methodName){
                        var paramInfos = subitem.paramInfoList;
                        if(paramInfos){
                            $.each(paramInfos, function (j, subitem1) {
                                var param = '<div class="form-group"><label class="control-label col-sm-1">' + subitem1.paramName + '</label><div class="col-sm-4"><input type="text" class="form-control" name="' + subitem1.paramName + '" placeholder="' + subitem1.paramType + '"></div><label class="control-label">参数样例：' + subitem1.defaultValue + '</label></div>';
                                $("#paramForm").append(param);
                            })
                        }
                        var button = '<div class="box-tools pull-right" ><button type="button" class="btn btn-info btn-sm" id="btnQuery" onclick="paramSearch();">查询</button></div>';
                        $("#paramForm").append(button);
                    }
                })
            }
        }
    })
}

function paramSearch() {
    var data = $("#paramForm").serializeArray();
    var serviceName = $('select[name="serviceName"]').val();
    var methodName = $('select[name="methodName"]').val();
    data.push({ 'name': "serviceName", 'value': serviceName });
    data.push({ 'name': "methodName", 'value': methodName });
    $.ajax({
        url: '/fragment?method=invoke',
        type: 'POST',
        dataType: 'json',
        data: data,
        success: function (data, textStatus, jqXHR) {
            $("#resultMsg").html(JSON.stringify(data));
            $("#serviceModal").modal('show');
        }
    });
}