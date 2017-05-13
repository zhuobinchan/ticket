$(function(){
	makeNavi();
});
function makeNavi(){
	  $(".tnav ul li .b3").mouseover(function(e){
		  $(this).parent("li").attr('class','hover');
		  $(".trbox").show();
	  }).mouseout(function(e){
		  var obj = this;
		  $(document).mousemove(function(e2){
			  var offset = $(obj).offset();//“设置”这个元素的位置
			  var divleft = $(".trbox").offset().left;
			  var divheight = $(".trbox").height();
			  var divtop = $(".trbox").offset().top;
			  if(e2.pageY<offset.top||e2.pageX<divleft||e2.pageY==(divtop+divheight-1)){
				  $(this).parent("li").attr('class','');
				  $(".trbox").hide();
			  }
		  });
	  });
	  $(".tnav ul li .b4").click(function(){
			$(".topbox").hide();
			$("#openmenu").show();
	  });
	  
	  $(".flinks .t").click(function(){
			$(".flinks span").toggle();
	  });
	  $("#openmenu").click(function(){
			$("#openmenu").hide();
			$(".topbox").show();
	  });
	  /*展开左侧导航栏*/
	  $(".mainlm ul li h2").click(function(){
		  if ( $(this).parent("li").attr('class')!=='hover' ){
			$(this).parent("li").siblings().attr('class','');
			$(this).parent("li").siblings().children("a").css("display","none");
			$(this).parent("li").attr('class','hover');
			$(this).parent("li").children("a").show();
			$(this).parent("li").children("a").css("display","block");
		  }else {
			$(this).parent("li").attr('class','');
			$(this).parent("li").children("a").css("display","none");
		  }
	  });
	  $(".lshulistm ul li h2 ,.tshulistm ul li h2").click(function(){
		  if ( $(this).parent("li").attr('class')!=='hover' ){
			$(this).parent("li").attr('class','hover');
			$(this).parent("li").children("a").show();
			$(this).parent("li").children("a").css("display","block");
		  }else {
			$(this).parent("li").attr('class','');
			$(this).parent("li").children("a").css("display","none");
		  }
	  });
}

function setTab(name,cursel,n){
 for(i=1;i<=n;i++){
  var menu=document.getElementById(name+i);
  var con=document.getElementById("tab_"+name+"_"+i);
  menu.className=i==cursel?"hover":"";
  con.style.display=i==cursel?"block":"none";
 }
}