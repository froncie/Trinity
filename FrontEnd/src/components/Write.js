import React, { Fragment } from "react";

function Write(){
    return <Fragment>
        <form action="/test" method="post" style={{width:'600px', height:'600px', marginLeft:"550px", marginTop:'50px'}}>
        <div class="form-group">
              <label for="exampleFormControlInput1">제목</label>
            <input type="text" class="form-control" id="exampleFormControlInput1" name="title" placeholder="제목을 작성해주세요."/>
          </div>
        <div class="form-group">
            <label for="exampleFormControlInput1">작성자</label>
            <input type="text" class="form-control" id="exampleFormControlInput1" name="crea_id" placeholder="이름을 적어주세요."/>
          </div>
          <div class="form-group">
            <label for="exampleFormControlTextarea1">내용</label>
            <textarea class="form-control" id="exampleFormControlTextarea1" name="contents" rows="10"></textarea>
          </div>
        <button type="submit" class="btn btn-info" style={{marginLeft:'475px'}}>등록</button>
        <button type="button" class="btn btn-secondary" style={{marginLeft:'5px'}}>목록</button>
    </form>
    </Fragment>

}

export default Write