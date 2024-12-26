console.log("admin user");

document.querySelector('#image_file_input').addEventListener('change', (e)=>{
    var file = e.target.files[0];
    var reader = new FileReader();
    reader.onload = function(){
        document.getElementById("upload_image_preview").src = reader.result;
    }
    reader.readAsDataURL(file);
})