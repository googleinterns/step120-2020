var placeSearch, autocomplete;

const componentForm = {
  street_number: 'short_name',
  route: 'long_name',
  locality: 'long_name',
  administrative_area_level_1: 'short_name',
  country: 'long_name',
  postal_code: 'short_name',
  lat: '',
  lng: ''
};

function initAutocomplete() {
  autocomplete = new google.maps.places.Autocomplete(
      document.getElementById('autocomplete'), {types: ['geocode']});
  autocomplete.setFields(['address_component', 'geometry']);
  autocomplete.addListener('place_changed', addPlaceToForm);
}

function addPlaceToForm() {
  const place = autocomplete.getPlace();

  for (const component in componentForm) {
    document.getElementById(component).value = '';
  }

  setDocElementsToAddressComponents(place);
  setDocElementsToGeometryComponents(place);
}

function setDocElementsToAddressComponents(place) {
  for (let i = 0; i < place.address_components.length; i++) {
    const addressType = place.address_components[i].types[0];
    if (componentForm[addressType]) {
      const val = place.address_components[i][componentForm[addressType]];
      document.getElementById(addressType).value = val;
    }
  }
}

function setDocElementsToGeometryComponents(place) {
  document.getElementById('lat').value = place.geometry.location.lat();
  document.getElementById('lng').value = place.geometry.location.lng();
}
