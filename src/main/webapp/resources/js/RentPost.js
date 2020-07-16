'use strict';
const google = window.google = window.google ? window.google : {}
const maps = google.maps = google.maps ? google.maps : {}
const places = maps.places = maps.places? maps.places : {}

/* global google */
class RentPost extends React.Component {
  constructor(props) {
    super(props);
    this.state = this.initState();

    this.handleAddressChange = this.handleAddressChange.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.autocomplete = null;
  }

  handleChange(event) {
    this.setState({[event.target.name]: event.target.value})
  }
  
  handleSubmit(event) {
    alert("a location was submitted: " + this.state.streetName);
    event.preventDefault();
  }

  componentDidMount() {
    this.autocomplete = new google.maps.places.Autocomplete(
        document.getElementById('autocomplete'), {types: ['geocode']});
    autocomplete.setFields(['address_component', 'geometry']);
    this.autocomplete.addListener('places_changed', this.handleAddressChange);
  }

  initState() {
    return {
      streetNumber: '',
      streetName: '',
      city: '',
      state: '',
      zipCode: '',
      country: '',
      lat: '',
      lon: ''
    }
  }

  handleAddressChange() {
    let place = this.autocomplete.getPlace();
    let address = place.address_components;

    for (var i = 0; i < address.length; i++) {
      let componentType = address[i].types[0];
      if (this.state.componentType) {
        let val = address[i].long_name
        this.setState({
          componentType: val
        })
      }
    }
  }

  render() {
  return React.createElement("div", null, React.createElement("input", {
    id: "autocomplete",
    className: "input-field",
    ref: "input",
    type: "text"
  }), React.createElement("input", {
    name: "number",
    value: this.state.streetNumber,
    placeholder: "Street Number",
    onChange: this.handleChange
  }), React.createElement("input", {
    name: "street_name",
    value: this.state.streetName,
    placeholder: "Street Name",
    onChange: this.handleChange
  }), React.createElement("input", {
    name: "city",
    value: this.state.city,
    placeholder: "City",
    onChange: this.handleChange
  }), React.createElement("input", {
    name: "state",
    value: this.state.state,
    placeholder: "State",
    onChange: this.handleChange
  }), React.createElement("input", {
    name: "zip_code",
    value: this.state.zipCode,
    placeholder: "Zipcode",
    onChange: this.handleChange
  }));
}
}

const domContainer = document.querySelector('#autocompleteForm');
ReactDOM.render(React.createElement(RentPost), domContainer);