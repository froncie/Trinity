import React, { Fragment } from "react";

function Reg(){
    return <Fragment>
    
        
    <main class="login-form">
    <div class="cotainer">
        <div class="row justify-content-center" >
            <div class="col-md-8" style={{paddingTop:"60px"}}>
                <div class="card">
                    <div class="card-header">Register</div>
                    <div class="card-body">
                        <form action="" method="">
                            <div class="form-group row">
                                <label for="email_address" class="col-md-4 col-form-label text-md-right"></label>
                                <div class="col-md-6">
                                    <label htmlFor="userId">아이디</label>
                                    <input type="text" id="id" class="form-control" name="userid"placeholder="아이디를 입력해주세요." required autofocus/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right"></label>
                                <div class="col-md-6">
                                <label htmlFor="userId">비밀번호</label>
                                    <input type="password" id="password" class="form-control" name="password" placeholder="비밀번호를 입력해주세요." required/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right"></label>
                                <div class="col-md-6">
                                <label htmlFor="password">비밀번호 확인</label>
                                    <input type="password" id="password" class="form-control" name="password" placeholder="비밀번호를 확인해주세요." required/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right"></label>
                                <div class="col-md-6">
                                <label htmlFor="email">이메일</label>
                                    <input type="email" id="email" class="form-control" name="eamil" placeholder="이메일을 확인해주세요." required/>
                                </div>
                            </div>

                            

                            <div class="col-md-6 offset-md-4">
                            <button type="submit" class="btn btn-primary" onclick="PostLoginData()" style={{Align:"center"}}>
                                    가입하기
                                </button>
                                
                            </div>
                            </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    </main>
    </Fragment>
}

export default Reg
