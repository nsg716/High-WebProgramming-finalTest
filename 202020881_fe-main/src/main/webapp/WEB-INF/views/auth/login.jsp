<html lang="ko">

<head>

<meta charset="UTF-8">

<meta name="viewport"content="width=device-width, initial-scale=1.0">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<title>Login Page</title>

<style>

	body {
	
			margin: 0;
			
			padding: 0;
			
			background-image: url('image/home-background.jpg'); /* • */
			
			background-size: cover;
			
			background-position: center; 
			
			font-family: 'Arial',sans-serif;
			
			display: flex;
			
			justify-content: center;
			
			align-items: center;
			
			height: 100vh;
	
	       }
	


	.login-form {
	
	background-color: rgba(255,255,255,0.9); 
	
	padding: 40px;
	
	border-radius: 10px;
	
	box-shadow: 0015pxrgba(0,0,0,0.1);
	
	width: 100%;
	
	max-width: 400px;
	
       }


	
	.login-formh1 {
	
	margin-bottom: 10px;
	
	color: #007bff;
	
	font-size: 28px;
	
	text-align: center;
	
        }



	.login-formh2 {
	
	margin-bottom: 20px;
	
	color: #007bff;
	
	font-size: 24px;
	
	       }



	.login-forminput[type="text"],
	
	.login-forminput[type="password"] {
	
	width: 100%;
	
	padding: 10px;
	
	margin-bottom: 20px;
	
	border: 1pxsolid#ccc;
	
	border-radius: 5px;
	
	font-size: 16px;
	
	      }



	.login-forminput[type="submit"] {
	
	width: 100%;
	
	padding: 10px;
	
	background-color: #007bff;
	
	color: white;
	
	border: none;
	
	border-radius: 5px;
	
	font-size: 18px;
	
	cursor: pointer;
	
        }


	
	.login-forminput[type="submit"]:hover {
	
	background-color: #0056b3;
	
	      }



	.error-message {
	
	color: red;
	
	margin-bottom: 20px;
	
	      }

</style>

</head>

<body>

		<div class="login-form">
		

		
			<h1>Webapp nsg716</h1>
			

			
			<h2>Login</h2>
			
			<c:if test="${not empty error}">
			    <p class="error-message">${error}</p>
			</c:if>

			
			<p class="error-message"></p>
			
			
			<form action="login"method="post">
				
				<input type="text"name="username"placeholder="Username"required>
				
				<input type="password"name="password"placeholder="Password"required>
				
				<input type="submit"value="Login">
			
			</form>
	
		</div>
	
	</body>

</html>