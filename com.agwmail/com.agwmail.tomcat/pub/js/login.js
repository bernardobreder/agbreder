function logo() {
	$('#center').html($('#logo').html());
}

function forgot_password() {
	$('#center').html($('#forgot_password').html());
}

function create_account() {
	$('#center').html($('#create_account').html());
}

function create_account_submit() {
	$("create_account_button").attr("disabled", true);
	var user = $('#create_account_username').val();
	var email = $('#create_account_email').val();
	var pass = $('#create_account_password').val();
	$.ajax({
		url : "create_account.do",
		data : {
			'username' : user,
			'email' : email,
			'password' : pass
		},
		success : function(data) {
			if (data == 'null') {
				alert("Username or Email already Used. Try another!");
			} else {
				logo();
			}
		},
		complete : function() {
			$("create_account_button").removeAttr("disabled");
		}
	});
}

function logining() {
	var user = $('#username').val();
	var pass = $('#password').val();
	$.ajax({
		url : "login.do",
		data : {
			'username' : user,
			'password' : pass
		},
		success : function(data) {
			data = JSON.parse(data);
			if (data != null) {
				window.open("index.html", "_self");
			} else {
				$('#password').val('');
			}
		}
	});
}

function login() {
	if (event.keyCode == '13') {
		logining();
	}
}

$(function() {
	$("#password").keyup(login);
	$("#username").keyup(login);
});