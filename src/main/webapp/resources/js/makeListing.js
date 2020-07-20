/**
* Calculates the total listing price.
* Method is called anytime there is an input change to an input that is
* involved in this calculation.
*/
function calculateListingPrice() {
  const numSingles = parseInt(document.getElementById("numSingles").value);
  const singlePrice = parseFloat(document.getElementById("singlePrice").value);
  const numShared = parseInt(document.getElementById("numShared").value);
  const sharedPrice = parseFloat(document.getElementById("sharedPrice").value);
  const listingPrice = numSingles * singlePrice + numShared * sharedPrice;
  if (isNaN(listingPrice)) {
    document.getElementById('listingPriceDisplay').innerHTML = "Total Listing Price: Invalid Entry For Input"
  } else {
    document.getElementById('listingPriceDisplay').innerHTML = "Total Listing Price: $" + listingPrice;
    document.getElementById('listingPrice').setAttribute('value', listingPrice);
  }
}
