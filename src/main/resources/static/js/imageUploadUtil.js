
const profileImageUpdate = document.querySelector("#update-profile-image");
const imageLink = profileImageUpdate.querySelector(".image-style");
const profileImageUpload = profileImageUpdate.querySelector("#profile-image-upload");
const updateButton = profileImageUpdate.querySelector("#update-button");

let clicked = false;


(function closeUpload() {

    imageLink.addEventListener("click", function(e) {
        e.preventDefault();
        if (!clicked) {
            profileImageUpload.style.display = "flex";
            clicked = true;
        } else {
            profileImageUpload.style.display = "none";
            clicked = false;
        }
    });

})();

document.querySelector("#attachment").addEventListener("change", function() {
    showImageThumbnail(this)
});

function showImageThumbnail(fileInput) {

    if (checkImageUploadSize(fileInput)) {

        const file = fileInput.files[0];
        const reader = new FileReader();

        reader.onload = function(e) {
            document.querySelector("#thumbnail").setAttribute("src", e.target.result);
        };
        reader.readAsDataURL(file);
    } else {
        fileInput.value = "";
    }

}

function checkImageUploadSize(fileInput) {

    const maxFileSize = 131072;
    const file = fileInput.files[0];

    if (file.size > maxFileSize) {

        alert('File too large. Maximum size is 128KB');

        return false;
    }
    updateButton.disabled = false;
    return true;
}