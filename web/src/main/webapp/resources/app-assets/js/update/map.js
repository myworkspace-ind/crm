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

        const streetAddress = document.getElementById("street-address").value.trim();
        const suburb = document.getElementById("suburb").value.trim();
        const state = document.getElementById("state").value.trim();
        const postcode = document.getElementById("postcode").value.trim();

        // Combine the address into a formatted string
        const fullAddress = `${streetAddress}, ${suburb}, ${state}, ${postcode}, Australia`;

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
    const streetAddress = document.getElementById("street-address");
    const suburb = document.getElementById("suburb");
    const state = document.getElementById("state");
    const postcode = document.getElementById("postcode");

    // Validate street address
    if (!streetAddress.value.trim()) {
        showError(streetAddress, "Street address is required.");
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

    // Validate postcode (must be a number)
    if (!/^\d+$/.test(postcode.value.trim())) {
        showError(postcode, "Postcode must be a number");
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

// Integration with Google Map
let map;
let marker;
let geocoder;
let autocomplete;
let modalAutocomplete;
let apiCallCount = 0;

/**
 * Initialize Google Map and its event listeners.
 * INPUT: None (uses default configuration for the map and autocomplete API)
 * PROCESSING:
 *  - Creates the Google Map centered at a default location.
 *  - Sets up a draggable marker.
 *  - Adds "click" event to map for marker placement.
 *  - Adds autocomplete functionality for the input field.
 * OUTPUT: Configured map with event listeners for marker updates and address lookups.
 */
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
        clickable: true
    });

    geocoder = new google.maps.Geocoder();

    // Main Autocomplete for address input
    autocomplete = new google.maps.places.Autocomplete(
        document.getElementById("address")
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

    // Add a click event to the map to update the marker and geocode the new position
    google.maps.event.addListener(map, "click", function (event) {
        // Get the clicked location
        const clickedLocation = event.latLng;

        // Move the marker to the clicked location
        marker.setPosition(clickedLocation);

        // Geocode the new position to update the address
        geocodePosition(clickedLocation);
    });

    // Attach event to the modal opening
    document.getElementById("address-modal").addEventListener("click", function () {
        setTimeout(setupModalAutocomplete, 500); // Delay to ensure modal is fully loaded
    });
}

// Function to setup Autocomplete in modal
function setupModalAutocomplete() {
    let streetAddress = document.getElementById("street-address");

    if (streetAddress) {
        // Always create a new instance when the modal opens
        modalAutocomplete = new google.maps.places.Autocomplete(streetAddress, {
            types: ["geocode"],
        });

        modalAutocomplete.addListener("place_changed", function () {
            let place = modalAutocomplete.getPlace();
            if (!place.geometry) {
                alert("No details available for this address.");
                return;
            }

            // Update the marker position
            marker.setPosition(place.geometry.location);
            marker.setMap(map);

            // Update the map center and zoom
            map.setCenter(place.geometry.location);
            map.setZoom(17);

            // Fill the modal form
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


// Toggle the form visibility based on the country
function toggleFormFields(isVietnam) {
    const vietnamForm = document.getElementById("vietnam-address-form");
    const nonVietnamForm = document.getElementById("default-address-form");

    if (isVietnam) {
        // Show Vietnam-specific form, hide others
        vietnamForm.style.display = "block";
        nonVietnamForm.style.display = "none";
    } else {
        // Show non-Vietnam form, hide others
        vietnamForm.style.display = "none";
        nonVietnamForm.style.display = "block";
    }
}

/**
 * Populate modal form fields based on the Google Place or Geocoder result.
 * INPUT:
 *  - place {Object} from Google Places/Geocoder API containing address components.
 * PROCESSING:
 *  - Extracts parts of an address (street address, suburb, state, etc.).
 *  - Updates corresponding modal fields with extracted values.
 * OUTPUT:
 *  - None (updates HTML input fields directly).
 */
function fillFormFields(place) {
    let streetAddress = "";
    let suburb = "";
    let state = "";
    let postcode = "";
    let country = "";
    let ward = "";
    let isVietnam = false;

    console.log("Address Components:", place.adr_address);

    // Check if the country is Vietnam
    isVietnam = place.address_components.some(component =>
        component.types.includes("country") && component.long_name === "Vietnam"
    );

    place.address_components.forEach((component) => {
        const types = component.types;

        if (types.includes("street_number")) streetAddress += component.long_name + " ";
        if (types.includes("route")) streetAddress += component.long_name;

        if (types.includes("country")) country = component.long_name;
        if (types.includes("postal_code")) postcode = component.long_name;

        if (isVietnam) {
            if (types.includes("administrative_area_level_2") || types.includes("sublocality")) {
                suburb = component.long_name;
            }
            if (types.includes("administrative_area_level_1")) {
                state = component.long_name;
            }
        } else {
            if (types.includes("locality") || types.includes("sublocality")) suburb = component.long_name;
            if (types.includes("administrative_area_level_1")) state = component.long_name;
        }
    });

    // Extract ward for Vietnam if available
    if (isVietnam && place.adr_address) {
        const tempDiv = document.createElement("div");
        tempDiv.innerHTML = place.adr_address;
        const wardSpan = tempDiv.querySelector(".extended-address");
        if (wardSpan) {
            ward = wardSpan.textContent.trim();
        }
    }

    console.log("Extracted Street Address:", streetAddress);
    console.log("Extracted Suburb (District):", suburb);
    console.log("Extracted State:", state);
    console.log("Extracted Postcode:", postcode);
    console.log("Extracted Ward:", ward);
    console.log("Extracted Country:", country);

    // Updating the form fields based on the country
    if (isVietnam) {
        // Vietnam-specific fields
        if (document.getElementById("street-address")) document.getElementById("street-address").value = streetAddress;
        if (document.getElementById("vn-ward")) document.getElementById("vn-ward").value = ward;
        if (document.getElementById("vn-district")) document.getElementById("vn-district").value = suburb;
        if (document.getElementById("vn-city")) document.getElementById("vn-city").value = state;
        if (document.getElementById("vn-country")) document.getElementById("vn-country").value = "Vietnam";
    } else {
        // Non-Vietnam fields
        if (document.getElementById("street-address")) document.getElementById("street-address").value = streetAddress;
        if (document.getElementById("suburb")) document.getElementById("suburb").value = suburb;
        if (document.getElementById("state")) document.getElementById("state").value = state;
        if (document.getElementById("postcode")) document.getElementById("postcode").value = postcode;
        if (document.getElementById("country")) document.getElementById("country").value = country;
    }

    // Toggle form visibility based on country (Vietnam or not)
    toggleFormFields(isVietnam);
}

// Assign initMap function to the global window object
window.initMap = initMap;
