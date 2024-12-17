
//for showing the view of contact profile
console.log("Contacts.js");
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

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactdata(id){

    //function call to load data
    console.log(id);
    
    try{
        const data = await (
            await fetch(`http://localhost:8081/api/contacts/${id}`)
        ).json();
        console.log(data);
        document.querySelector("#contact_name").innerHTML = data.name;
        document.querySelector("#contact_email").innerHTML = data.eamil;
        document.querySelector("#contact_image").src = data.picture;

        document.querySelector("#contact_address").innerHTML = data.address;
        document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
        document.querySelector("#contact_email").innerHTML = data.description;
        const contactFavorite = document.querySelector("#contact_favorite");
        if(data.favorite){
            contactFavorite.innerHTML =
            "<i class='fas fa-star text-yellow-400'><i class='fas fa-star text-yellow-400'><i class='fas fa-star text-yellow-400'>"
        }else{
            contactFavorite.innerHTML = "Not Favorite Contact";
        }

        document.querySelector("#contact_website").href = data.websiteLink;
        document.querySelector("#contact_website").innerHTML = data.websiteLink;
        document.querySelector("#contact_linkedIn").href = data.linkedInLink;
        document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
        openContactModal();
    } catch(error){
        console.log("Error: ", error);
    }
     
}