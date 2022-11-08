import React, {Fragment} from "react";

function Qna(){
    return <Fragment>
        <h2 style={{textAlign:"center", paddingTop:'20px' }}>QnA 게시판 </h2>
        <br></br>
        <table border="1" width="700" height ="300" align="center" >
   
   <tr>
       <td width="100" align="center">Author</td>
       <td><form id="form_textarea" name="form_textarea">
           <textarea  name="content" rows="2" style={{width:'98%'}}></textarea>
       
       </form>
   </td>
       <td width="100" align="center">Register</td>
       <td><form id="form_textarea" name="form_textarea">
           <textarea  name="content" rows="2" style={{width:'98%'}}></textarea>
       
       </form></td>
   </tr>
   <tr>
       <td width="100" align="center">Email</td>
       <td><form id="form_email" name="form_email" style={{paddingLeft:'5px'}}>
           <input type={'email'} id="email" name="email" style={{width:'98%'}} />
       
       </form></td>
       <td width="100" align="center">Views</td>
       <td><form id="form_textarea" name="form_textarea">
           <textarea  name="content" rows="2" style={{width:'98%'}}></textarea>
       
       </form></td>
   </tr>
   <tr>
       <td width="100" align="center">Subject</td>
       <td colspan="3"><form id="form_textarea" name="form_textarea">
           <textarea  name="content" rows="2" style={{width:'98%'}}></textarea>
       
       </form></td>
   </tr>
   <tr>
       <td width="100" align="center">Contents</td>
       <td colspan="3"><form id="form_textarea" name="form_textarea">
           <textarea  name="content" rows="2" style={{width:'98%'}}></textarea>
       
       </form></td>
   </tr>
   <tr>
       <td width="100" align="center">File Upload</td>
       <td colspan="3">
        <form action="" method="post" encType="multipart/form-data" id="form_file" name="form_file">
           <input type={'file'} id="file" name="file" style={{width:'98%'}}/>
       </form></td>
   </tr>
   <tr>
       <td colspan="4" style={{textalign:'center'}}>Delection Board List</td>

   </tr>
  </table>
  <div style={{textAlign:'center', paddingTop:'10px'}}>
    <form action="/test" method="post">
        <button type="submut" value={'sumbit'} style={{height:'50px', width:'100px' }}>submit</button>
    </form>
  </div>
        
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

export default Qna
