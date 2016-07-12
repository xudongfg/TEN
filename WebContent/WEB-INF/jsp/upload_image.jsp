<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta charset="UTF-8">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">

<title>Tribal Education Network Image Upload</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-73779770-1', 'auto');
  ga('send', 'pageview');
	</script>
<script src="<c:url value="/js/jquery.scrolldepth.min.js"/>"></script>
<script>
	jQuery(document).ready(function(){
		jQuery(function() {
			jQuery.scrollDepth();
		});
		/*
		jQuery.scrollDepth({
			eventHandler: function(data) {
				console.log(data)
			}
		});
		*/
	});
	</script>
<script>
		function onFileUpload(){
			//check for valid file type
			var supportedFileTypes = ['image/jpeg','image/gif','image/jpg','image/png','image/bmp'];
			var file = document.getElementById("upload").files[0];
			if(supportedFileTypes.indexOf(file.type) == -1){
				alert("File type not supported");
				$("#uploadPreview_div").empty();
				$("#uploadPreview_div").hide();
				document.getElementById("upload").value = '';
				$("#annotations_div").hide();
				return false;
			}
			$("#uploadPreview_div").show();
			$("#annotations_div").show();
			
			var oFReader = new FileReader();
			oFReader.readAsDataURL(file);			
			oFReader.onload = function (_file) {
		        var obj = document.createElement('img');
				$(obj).attr('src', _file.target.result);
				$(obj).attr('width', '200');
				$(obj).attr('height', '200');
					
		  $("#uploadPreview_div").empty();
		  if(file.size > 102400000000000000000000000){
					$("#uploadPreview_div").append('<p style="color:red;"><b>Preview not available as the file size is large<b><br><br>');
				}else{
					$("#uploadPreview_div").append(obj);
				}
		  };
		}
		
		function validateForm(){			
			document.getElementsByTagName("form").submit(); 
		}
	</script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_intaker.jpg');background-attachment: fixed; background-position: right bottom;background-repeat:no-repeat">
	<form
		action="${pageContext.request.contextPath}/upload/uploadimage.action"
		method="post" enctype="multipart/form-data">
		<%@include file="include_header.jsp"%>

		<section>
			<table>
				<tr>
					<td><s:if test="hasActionErrors()">
							<s:actionerror />
						</s:if></td>
				</tr>
			</table>

			<table>
				<tr>
					<td><s:if test="hasActionMessages()">
							<s:actionmessage />
						</s:if></td>
				</tr>
			</table>

			<table style="width: 700px">
				<tr>
					<td><input type="file" id="upload" name="upload" size="50"
						onchange="onFileUpload()" /></td>
					<td></td>
				</tr>
				<tr>
					<td>The following file types can be uploaded: .jpeg, .gif,
						.jpg, .png, .bmp</td>
				</tr>
				<tr>
					<td><div id="uploadPreview_div" style="display: none;">
						</div></td>
					<td></td>
				</tr>
				<tr>
					<td>
						<div id="annotations_div" style="display: none;">
							<%@include file="digital_rights_management.jsp"%>
						</div>
					</td>
				</tr>
				<tr>
					<td><input type="submit" value="Upload Image"
						onclick="validateForm();" /></td>
				</tr>
			</table>
		</section>

	</form>
</body>
</html>