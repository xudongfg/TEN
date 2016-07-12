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
		function onFileUpload(){
			//check file type		
			var supportedFileTypes = ['text/plain', 'application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
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
			oFReader.onload = function(_file) {				
				var obj,embed;		
				obj = document.createElement('object');
				$(obj).attr('data', _file.target.result);
				$(obj).attr('width', '100%');
				$(obj).attr('height', '400px');
				$(obj).attr('type', file.type);
				
				embed = document.createElement('embed');
				$(embed).attr('src', _file.target.result);
				$(embed).attr('type', file.type);
								
				$("#uploadPreview_div").empty();
				if(file.size > 11000000000000){
					$("#uploadPreview_div").append('<p style="color:red;"><b>Preview not available as the file size is large<b><br><br>');
				}else{
					$("#uploadPreview_div").append(obj);
					$(obj).append(embed);
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
		action="${pageContext.request.contextPath}/upload/uploadtext.action"
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
				</tr>
				<tr>
					<td>The following file types can be uploaded: .txt, .pdf,
						.docx, .doc</td>
				</tr>
				<tr>
					<td style="width: 60%;"><div id="uploadPreview_div"
							style="display: none;"></div></td>
				</tr>
				<tr>
					<td>
						<div id="annotations_div" style="display: none">
							<%@include file="digital_rights_management.jsp"%>
						</div>
					</td>
				</tr>

				<tr>
					<td><input type="submit" value="Upload Text"
						onclick="validateForm();" /></td>
				</tr>
			</table>
		</section>
	</form>
</body>
</html>