<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>jQuery contextMenu</title>

    <link href="<%=path %>/static/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=path %>/static/js/jquery.js"></script>
    <script src="<%=path %>/static/dist/jquery.contextMenu.min.js" type="text/javascript"></script>



</head>
<body class="wy-body-for-nav">

<div class="wy-grid-for-nav">
    <section data-toggle="wy-nav-shift" class="wy-nav-content-wrap">

                <div class="wy-nav-content">
            <div class="rst-content">
                <div role="main" class="document">
                                        <a href="https://github.com/swisnl/jQuery-contextMenu" class="edit-on-github fa fa-github"> Fork on GitHub</a>
                    <h1 id="demo-simple-context-menu">Demo: Simple Context Menu</h1>
<p><span class="context-menu-one btn btn-neutral">right click me</span></p>
<h2 id="example-code-simple-context-menu">Example code: Simple Context Menu</h2>
<script type="text/javascript" class="showcase">
    $(function() {
        $.contextMenu({
            selector: '.context-menu-one', 
            callback: function(key, options) {
                var m = "clicked: " + key;
                window.console && console.log(m) || alert(m); 
            },
            items: {
                "edit": {name: "Edit", icon: "edit"},
                "cut": {name: "Cut", icon: "cut"},
               copy: {name: "Copy", icon: "copy"},
                "paste": {name: "Paste", icon: "paste"},
                "delete": {name: "Delete", icon: "delete"},
                "sep1": "---------",
                "quit": {name: "Quit", icon: function(){
                    return 'context-menu-icon context-menu-icon-quit';
                }}
            }
        });

        $('.context-menu-one').on('click', function(e){
            console.log('clicked', this);
        })    
    });
</script>
<h2 id="example-html-simple-context-menu">Example HTML: Simple Context Menu</h2>
<div style="display:none;" class="showcase" data-showcase-import=".context-menu-one"></div>
<h2 id="jquery-context-menu-demo-gallery">jQuery Context Menu Demo Gallery</h2>
                </div>


            </div>
        </div>

    </section>

</div>

</body>
</html>
