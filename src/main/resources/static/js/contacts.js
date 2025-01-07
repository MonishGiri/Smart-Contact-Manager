console.log('contact.js');

const baseURL = "http://localhost:8081"
const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal,options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(userId){
    console.log('hello')
    // function call to load data
    try {
        const data = await (await fetch(`${baseURL}/api/contacts/${userId}`)).json();
        console.log(data);
        console.log(data.name);
        document.querySelector('#contact_name').innerHTML = data.name;
        document.querySelector('#contact_email').innerHTML = '<i class="fa-solid fa-envelope"></i> '+data.email;
        document.querySelector('#contact_phone').innerHTML = '<i class="fa-solid fa-phone"></i>'+data.phoneNumber;
        document.querySelector('#contact_address').innerHTML = data.address;
        document.querySelector('#contact_about').innerHTML = data.description;
        const favorite = document.querySelector('#contact_favorite');
        favorite.innerHTML = data.favorite ? '<i class="fa-solid fa-star text-yellow-300"></i>' : 'Not Favorite Contact';
        const linkedIn = document.querySelector('#contact_linkedIn');
        linkedIn.setAttribute('href',data.linkedInLink);
        linkedIn.textContent = data.linkedInLink;

        const website = document.querySelector('#contact_website');
        website.setAttribute('href',data.websiteLink);
        website.textContent = data.websiteLink;
        
        const img = document.getElementById('contact_image');
        img.setAttribute('src',data.picture);
        console.log(img);
        
    } catch (error) {
        console.log(error);
    }
    openContactModal();

}

// delete contact

async function deleteContact(id){
Swal.fire({
  title: "Do you want to Delete the Contact?",
  icon: "warning",
  showCancelButton: true,
  cancelButtonColor:"#808080",
  confirmButtonText: "Delete",
  confirmButtonColor:"#880808"
}).then((result) => {
  /* Read more about isConfirmed, isDenied below */
  if (result.isConfirmed) {
    const url = baseURL+"/user/contact/delete/"+id;
    window.location.replace(url);
  } 
});
}