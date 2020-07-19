function calculateListingPrice() {
  let numSingles = parseInt(document.getElementById("numSingles").value);
  let singlePrice = parseFloat(document.getElementById("singlePrice").value);
  let numShared = parseInt(document.getElementById("numShared").value);
  let sharedPrice = parseFloat(document.getElementById("sharedPrice").value);
  let listingPrice = numSingles * singlePrice + numShared * sharedPrice;
  if (isNaN(listingPrice)) {
    document.getElementById('listingPriceDisplay').innerHTML = "Total Listing Price: Invalid Entry For Input"
  } else {
    document.getElementById('listingPriceDisplay').innerHTML = "Total Listing Price: $" + listingPrice;
    document.getElementById('listingPrice').setAttribute('value', listingPrice);
  }
}
