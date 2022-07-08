/***
 * created by 北大课工场
 * 加载订单详细信息
 * @param number
 * @param price
 */
$(function() {
    var orderId=GetQueryString("orderId");
    $.ajax({
        url: ReqUrl.OrdeReqUrl()+"/v/prepay/",
        type:"POST",
        data:{"orderId":orderId,"token":getCookie("token")},
        dataType: "json",
        success: function (response, status, xhr) {
           if(response.code==0){
               var order=response.data;
               $('.price').html(order.amount);
               $('.number').html(order.orderNo);
               $('#orderNo').val(order.orderNo);
               $('#goodsId').val(order.goodsId);
               $('#payAmount').val(order.amount);
               $("input[name='payType']").click(function () {
                   var value=$(this).val();
               });
           }
        }
    });
    //立即支付点击事件
    $("#submitBtnBank").click(function(){
        var flag = $("input[name='payType']:checked").val();
        if(null == flag){
            alert("请选择支付方式！");
        }else if(1==flag){ //支付宝支付
            window.open(ReqUrl.PayReqUrl()+"v/toPay?orderId="+orderId+"&token="+getCookie("token"));
            $(".box_kouKuan").show();
        }else if(2 == flag){ //微信支付
            window.open("wxCode.html?orderId="+orderId);
            $(".box_kouKuan").show();
        }
    });
});

//支付成功事件
function paySuccess(){
    window.location.href = "/index.html";
}
function payFail(){
    $(".box_kouKuan").hide();
}
//弹出窗口关闭
function closeWindow(){
    CloseDiv("showdiv","bgdiv");
}
//支付提示
function payNotice() {
  $(".box_kouKuan").show();
}
