import React,{Fragment} from "react";

function Login(){
    return <Fragment>
<nav className="navbar navbar-expand-lg navbar-light navbar-laravel">
    <div className="container">
        <a className="navbar-brand" href="#">Trinity</a>
        <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav ml-auto">
                <li className="nav-item">
                    <a className="nav-link" href="#">Login</a>
                </li>
                <li className="nav-item">
                    <a className="nav-link" href="#">Register</a>
                </li>
            </ul>

        </div>
    </div>
</nav>

<main className="login-form">
    <div className="cotainer">
        <div className="row justify-content-center">
            <div className="col-md-8">
                <div className="card">
                    <div className="card-header">Login</div>
                    <div className="card-body">
                        <form action="" method="">
                            <div className="form-group row">
                                <label for="email_address" className="col-md-4 col-form-label text-md-right"></label>
                                <div className="col-md-6">
                                    <input type="text" id="id" className="form-control" name="id" placeholder="아이디" required autofocus/>
                                </div>
                            </div>

                            <div className="form-group row">
                                <label for="password" className="col-md-4 col-form-label text-md-right"></label>
                                <div className="col-md-6">
                                    <input type="password" id="password" className="form-control" name="password" placeholder="비밀번호" required/>
                                </div>
                            </div>

                            <div className="form-group row">
                                <div className="col-md-6 offset-md-4">
                                    <div className="checkbox">
                                        <label>
                                            <input type="checkbox" name="remember"/> 로그인 정보 기억하기
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div className="col-md-6 offset-md-4">
                                <button type="submit" className="btn btn-primary" onclick="PostLoginData()">
                                    로그인
                                </button>
                                <a href="#" className="btn btn-link">
                                    아이디/비밀번호 찾기
                                </a>
                            </div>
                            </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</main>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css"/>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>


<link rel="dns-prefetch" href="https://fonts.gstatic.com"/>
<link href="https://fonts.googleapis.com/css?family=Raleway:300,400,600" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" href="css/style.css"/>

<link rel="icon" href="Favicon.png"/>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"/>
</Fragment>


}

export default Login
