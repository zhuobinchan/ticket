

var showLeft = function(id){
	$.ajax({
		url:g_path+"/public/findLeftNavis.htmls",
		dataType:"json",
		type:"POST",
		data:{id:id},
		success:function(ret){
			$('#mainFrame').height(availHeight);
			$(".mainlm ul").html("");
			for(var i=0;i<ret.length;i++){
				var navi = ret[i];
				var li = $("<li><h2 class='b"+navi.imgPath+"'>"+navi.name+"</h2></li>");
				var children = navi.children;
				for(var j=0;j<children.length;j++){
					var navi2 = children[j];
					if(navi2.pageUrl.indexOf(".htmls")==-1){
						li.append("<a href='#' url='"+g_path+"/public/common/r.htmls?page="+navi2.pageUrl + "&naviId="+navi2.id+"' target='mainFrame'><img src='static/images/lbc"+navi2.imgPath+".png'/>"+navi2.name+"</a>");
					}else{
						li.append("<a href='#' url='"+g_path+"/" + ((navi2.pageUrl).replace("new:","")) + (navi2.pageUrl.indexOf('?') >= 0 ? '&' : '?') + "v=1' target='mainFrame'><img src='static/images/lbc"+navi2.imgPath+".png'/>"+navi2.name+"</a>");
					}
					
				}
				$(".mainlm ul").append(li);
			}
			$(".mainlm ul").append($("<div class='clear'></div>"));
			makeNavi();
			$(".mainlm ul a").click(function(e){//触发左部导航事件
				e.preventDefault();
				var url = $(this).attr('url') + "&dev="+new Date();
				$('#mainFrame').attr('src',url);
				$(this).parents("ul").find("a").removeClass("hover");
				$(this).addClass("hover");
			})
			//var h = $(top).height() - $('#header').height() - $('#footer').height();
			//$("iframe[name=mainFrame]").height(h+20);
			//$("div.mainl").height(h);
		}
	})
}
var modPassword = function(e){
	e.preventDefault();
	layer.open({
	    type: 2,
	    title:'修改个人密码',
	    skin: 'layui-layer-rim', //加上边框
	    area: ['600px', '400px'], //宽高
	    content: g_path+'/public/common/r2.htmls?page=abc/home/modpassword'
	});
}

//注销
var logout = function(e){
	e.preventDefault();
	$.ajax({
		url:g_path+'/public/logoutSys.htmls',
		type:'POST',
		dataType:'html',
		success:function(ret){
			window.location.href = "${pageContext.request.contextPath}/index.htmls";
		}
	});
}
var closeSys = function(e){
	e.preventDefault();
	$.ajax({
		url:g_path+'/public/logoutSys.htmls',
		type:'POST',
		dataType:'html',
		success:function(ret){
			window.close();
		}
	});
}
//两个方法后缀不一样
//创建快捷方式一
function toDesktop(sUrl,sName){ 
	try { 
		var WshShell = new ActiveXObject("WScript.Shell"); 
		//在指定的文件夹下创建名为sName的快捷方式
		var oUrlLink = WshShell.CreateShortcut(WshShell.SpecialFolders("Desktop") + "\\" + sName + ".url");		
		//快捷方式指向的链接 
		oUrlLink.TargetPath = sUrl; 
		oUrlLink.Save(); 
	}catch(e){ 
	    alert(e.message);
		//alert("当前IE安全级别不允许操作！");
		//最简单解决方法：打开Internet Explorer “工具”菜单栏中的“选项”一栏，单击“安全”栏中的“自定义级别”选项卡，
		//将第三项“对没有标记为安全的activex控件进行初始化和脚本运行”设置成启用		
	} 
} 
function createDesktop(sUrl,sName)
{
    try
    {
        var fso = new ActiveXObject("Scripting.FileSystemObject");
        var shell = new ActiveXObject("WScript.Shell");
        var folderPath = shell.SpecialFolders("Desktop") ;//获取桌面本地桌面地址
        if(!fso.FolderExists(folderPath))
        {
            fso.CreateFolder(folderPath);
        }
        if(!fso.FileExists(folderPath + "\\"+sName+".lnk"))
        {
            //在指定的文件夹下创建名为sName的快捷方式            
			var shortLink = shell.CreateShortcut(folderPath + "\\"+sName+".lnk"); 
			//相应的描述信息
            shortLink.Description = sName; 
			//快捷方式指向的链接
            shortLink.TargetPath = sUrl; 
			//激活链接并且窗口最大化
            shortLink.WindowStyle = 3;
            shortLink.Save();
            alert('桌面快捷方式创建成功！');
        }
    }catch(e){
		alert(e.message);
        //alert("当前IE安全级别不允许操作！");
    }
}
//高度
var availHeight = window.screen.availHeight;
var availWidth = window.screen.availWidth-22;
var leftWidth = 180;

if(availWidth>1130){
	leftWidth = 200;
}
var rightWidth = availWidth-leftWidth;
window.onload=function(){
	$("div.nav a").click(function(e){
		e.preventDefault();
		var id = $(this).attr("id");
		showLeft(id);
	})
	$('#downbtn .b1').click(modPassword);
	//$("div.trboxm .b2").bind("click",logout);
	$("div.trboxm .b3").bind("click",function(e){
		toDesktop("http://"+window.location.host+g_path,"红太阳票务");
	});
	var height = window.screen.availHeight;
	$('#mainFrame').height(availHeight-$("#header").height()-10);
	$('div.mainr').width(rightWidth);
	$('div.mainl').width(leftWidth);
	$('div.topbox').width(availWidth);
	$('div#mainer').width(availWidth);
}
