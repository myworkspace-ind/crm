const addressModal = document.getElementById("address-modal");
const openModalButton = document.getElementById("address-btn");
const closeModalButton = document.getElementById("close-modal");
const customerAddressInput = document.getElementById("address");
const addressForm = document.getElementById("address-form");
const addressDisplay = document.getElementById("address-display");

// Show the modal
openModalButton.addEventListener("click", () => {
    addressModal.style.display = "block";
});

// Close the modal
closeModalButton.addEventListener("click", () => {
    addressModal.style.display = "none";
});


// Wait for the DOM to load before adding event listeners
document.addEventListener("DOMContentLoaded", function () {
    const addressForm = document.getElementById("address-form");
    const customerAddressInput = document.getElementById("address");
    const addressModal = document.getElementById("address-modal");

    if (!addressForm) {
        console.error("Address form not found!");
        return;
    }

    // Submit the address form
    addressForm.addEventListener("submit", (event) => {
        event.preventDefault(); // Prevent default form submission

        // Validate address before updating the address field
        if (!validateForm()) {
            console.error("Form validation failed!");
            return; // Stop execution if validation fails
        }

        const streetNumber = document.getElementById("street-number").value.trim();
        const streetName = document.getElementById("street-name").value.trim();
        const suburb = document.getElementById("suburb").value.trim();
        const state = document.getElementById("state").value.trim();
        const postcode = document.getElementById("postcode").value.trim();

        // Combine the address into a formatted string
        const fullAddress = `${streetNumber} ${streetName}, ${suburb}, ${state}, ${postcode}, Australia`;

        // Set the customer address input field
        customerAddressInput.value = fullAddress;

        // Geocode the address to get latitude and longitude
        geocoder.geocode({ address: fullAddress }, (results, status) => {
            if (status === google.maps.GeocoderStatus.OK) {
                const location = results[0].geometry.location;

                // Move the marker to the new location
                marker.setPosition(location);
                marker.setMap(map);

                // Center and zoom the map
                map.setCenter(location);
                map.setZoom(17);
            } else {
                alert("Could not find location: " + status);
            }
        });

        // Close the modal
        addressModal.style.display = "none";
    });

});

// Close modal when clicking outside the modal content
window.addEventListener("click", (event) => {
    if (event.target === addressModal) {
        addressModal.style.display = "none";
    }
});


// Validate the address modal
function validateForm() {
    console.log("validateAddress function is running!");
    let isValid = true;

    clearErrors(); // Reset previous errors

    // Get input fields
    const unitNumber = document.getElementById("unit-number");
    const streetNumber = document.getElementById("street-number");
    const streetName = document.getElementById("street-name");
    const streetType = document.getElementById("street-type");
    const suburb = document.getElementById("suburb");
    const state = document.getElementById("state");
    const postcode = document.getElementById("postcode");

    // Validate optional unit number (allow alphanumeric, dashes, and slashes)
    if (unitNumber.value && !/^[a-zA-Z0-9\s/-]+$/.test(unitNumber.value)) {
        showError(unitNumber, "Unit number can only contain letters, numbers, dashes, or slashes.");
        isValid = false;
    }

    // Validate street number (must be a number)
    if (!streetNumber.value.trim() || !Number.isInteger(Number(streetNumber.value))) {
        showError(streetNumber, "Street number must be a valid number.");
        isValid = false;
    }

    // Validate required fields
    if (!streetName.value.trim()) {
        showError(streetName, "Street name is required.");
        isValid = false;
    }

    if (!streetType.value.trim()) {
        showError(streetType, "Street type is required.");
        isValid = false;
    }

    if (!suburb.value.trim()) {
        showError(suburb, "Suburb is required.");
        isValid = false;
    }

    if (!state.value) {
        showError(state, "State is required.");
        isValid = false;
    }

    // Validate postcode (must be exactly 4 digits)
    if (!/^\d{4}$/.test(postcode.value.trim())) {
        showError(postcode, "Postcode must be exactly 4 digits.");
        isValid = false;
    }

    return isValid;
}

function showError(input, message) {
    // Find the parent form-group div
    const formGroup = input.closest(".form-group");

    // Remove existing error message before adding a new one
    const existingError = formGroup.querySelector(".error-message");
    if (existingError) {
        existingError.remove();
    }

    // Create error message span
    const errorText = document.createElement("span");
    errorText.classList.add("error-message");
    errorText.style.color = "red";
    errorText.textContent = message;

    // Append the error message to the form-group
    formGroup.appendChild(errorText);

    // Apply red border to the input field
    input.style.borderColor = "red";
}

function clearErrors() {
    // Remove all displayed error messages
    document.querySelectorAll(".error-message").forEach(e => e.remove());

    // Reset border colors for all inputs
    document.querySelectorAll("input, select").forEach(input => input.style.borderColor = "");
}


//integration google map
let map;
let marker;
let geocoder;
let autocomplete;
let modalAutocomplete;
let apiCallCount = 0;

function initMap() {
    // Default location (Sydney, Australia)
    const defaultLocation = { lat: -33.8568, lng: 151.2153 };

    // Initialize the map
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 15,
        center: defaultLocation,
    });

    // Initialize the marker at the default location
    marker = new google.maps.Marker({
        position: defaultLocation,
        map,
        draggable: true,
    });

    geocoder = new google.maps.Geocoder();

    // Main Autocomplete for address input
    autocomplete = new google.maps.places.Autocomplete(
        document.getElementById("address"),
        { types: ["geocode"], componentRestrictions: { country: "AU" } }
    );

    autocomplete.addListener("place_changed", function () {
        apiCallCount++; // Increment counter
        console.log(`Google Places API called: ${apiCallCount} times`);
        let place = autocomplete.getPlace();
        if (!place.geometry) {
            alert("No details available for the selected address.");
            return;
        }

        map.setCenter(place.geometry.location);
        map.setZoom(17);
        marker.setPosition(place.geometry.location);
        marker.setMap(map);

        document.getElementById("address").value = place.formatted_address;
        fillFormFields(place);
    });

    // Marker drag event to update address
    google.maps.event.addListener(marker, "dragend", function () {
        geocodePosition(marker.getPosition());
    });

    // Attach event to the modal opening
    document.getElementById("address-modal").addEventListener("click", function () {
        setTimeout(setupModalAutocomplete, 500); // Delay to ensure modal is fully loaded
    });

    // Process event click on the map
    map.addListener('click', function(e) {
        let label = 'A';
        var marker = new google.maps.Marker({
                position: e.latLng,
                label: label,
                map: map
        });

        // Debug
        console.log("Location at " + e.latLng + " with label " + label);
        
        // https://developers.google.com/maps/documentation/javascript/geocoding
        // Convert location (lat, lng) into address
        var geocoder = new google.maps.Geocoder();

        geocoder.geocode({ 'location': e.latLng }, function(results, status) {
            if (status === 'OK') {
                if (results[0]) {
                    console.log(results[0].formatted_address);

                    // Add new address into the handsontable in the left side
                    selectAddress(label, results[0].formatted_address, e.latLng.lat(), e.latLng.lng());
                } else {
                    console.log('No results found');
                }
            } else {
                console.log('Geocoder failed due to: ' + status);
            }
        });
    });
}

/**
 * Add new address into the form.
 * @param label Character of the label at the selected address. Ex: "A"
 * @param address full address. Ex: Australian Rockery Lawn, 2A Macquarie St, Sydney NSW 2000, Australia
 * @param lat Latitude of the selected address. Ex: -33.859063280483426
 * @param lng Longiture of the selected address. Ex: 151.2158149841309
 * 
 * @returns None
 * The inputs of address in the left form are filled.
 * The full address in the parent form is filled.
 * The latitude and longitude of the address are kept also.
 */
function selectAddress(label, address, lat, lng) {
    console.log("label, address, lat, lng = " + label + ": " + address + ": " + lat + ": " + lng);
}

// Function to setup Autocomplete in modal
function setupModalAutocomplete() {
    let streetName = document.getElementById("street-name");

    if (streetName) {
        // Always create a new instance when the modal opens
        modalAutocomplete = new google.maps.places.Autocomplete(streetName, {
            types: ["geocode"],
            componentRestrictions: { country: "AU" }
        });

        modalAutocomplete.addListener("place_changed", function () {
            let place = modalAutocomplete.getPlace();
            if (!place.geometry) {
                alert("No details available for this address.");
                return;
            }
            fillFormFields(place);
        });
    }
}

// Attach event when modal opens
document.getElementById("address-btn").addEventListener("click", function () {
    setTimeout(setupModalAutocomplete, 500); // Ensure modal is loaded
});


// Reverse geocode to update form when marker moves
function geocodePosition(pos) {
    geocoder.geocode({ location: pos }, function (results, status) {
        if (status === google.maps.GeocoderStatus.OK) {
            if (results[0]) {
                document.getElementById("address").value = results[0].formatted_address;
                fillFormFields(results[0]);

                map.setCenter(pos);
                map.setZoom(17);
            }
        }
    });
}

// Extract and fill form fields
function fillFormFields(place) {
    let streetNumber = "";
    let streetName = "";
    let streetType = "";
    let suburb = "";
    let state = "";
    let postcode = "";
    let country = "Australia";

    console.log("Address Components:", place.address_components); // Debugging

    place.address_components.forEach((component) => {
        const types = component.types;

        if (types.includes("street_number")) streetNumber = component.long_name;
        if (types.includes("route")) {
            streetName = component.long_name;
            let streetParts = streetName.split(" ");
            if (streetParts.length > 1) {
                streetType = streetParts.pop(); // Extract last word as street type
                streetName = streetParts.join(" ");
            }
        }
        if (types.includes("locality") || types.includes("sublocality")) suburb = component.long_name;
        if (types.includes("administrative_area_level_1")) state = component.short_name;
        if (types.includes("postal_code")) postcode = component.long_name;
        if (types.includes("country")) country = component.long_name;
    });

    console.log("Extracted Street Name:", streetName);
    console.log("Extracted Street Type:", streetType);

    if (document.getElementById("street-name")) document.getElementById("street-name").value = streetName;
    if (document.getElementById("street-type")) document.getElementById("street-type").value = streetType;
    if (document.getElementById("street-number")) document.getElementById("street-number").value = streetNumber;
    if (document.getElementById("suburb")) document.getElementById("suburb").value = suburb;
    if (document.getElementById("state")) document.getElementById("state").value = state;
    if (document.getElementById("postcode")) document.getElementById("postcode").value = postcode;
    if (document.getElementById("country")) document.getElementById("country").value = country;
}

// Assign initMap function to the global window object
window.initMap = initMap;
