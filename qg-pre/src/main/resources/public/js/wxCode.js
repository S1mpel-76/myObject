/***
 * created by 北大课工场
 * 加载订单详细信息
 * @param number
 * @param price
 */
var timer2;
$(function() {
    var orderId = GetQueryString("orderId");
    $.ajax({
        url: ReqUrl.PayReqUrl()+"/v/reqWxCode",
        method: "post",
        data:{"orderId":orderId,"token":getCookie("token")},
        success: function (data) {
            if(data.code==0){
                new QRCode(document.getElementById('qrcode'), data.data.codeUrl);
                timer2=window.setInterval(function(){
                    flushOrder(orderId)
                },1000);
            }else{
                alert("提示失败");
            }
        }
    });
});
function flushOrder(orderId) {
    $.ajax({
        url: ReqUrl.PayReqUrl()+"/v/checkOrderSuccess",
        method: "post",
        data:{"orderId":orderId,"token":getCookie("token")},
        success: function (result) {
            if(result.code==0){
                if(result.data.flag){
                    window.location.href="http://localhost:8888/paySuccess.html?orderId="+orderId;
                }
            }
        }
    });
}

