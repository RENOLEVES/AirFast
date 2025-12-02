<template>
  <div class="seat-price-container">
    <div class="content-area">
      <div class="card-background"></div>
      <div class="title">Set Seat Price</div>

      <div class="price-input-wrapper">
        <label for="new-price-input" class="input-label">New Price</label>
        <input type="text" id="new-price-input" class="price-input" placeholder="e.g. $50.00">
      </div>

      <button class="set-price-button">
        <span class="button-text">Set Price</span>
      </button>

      <div class="divider"></div>

      <div class="close-icon-placeholder"></div>
      <div class="misc-element"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SetSeatPriceForm',
  // You would add data, methods, computed properties here
  data() {
    return {
      // Example data
      newPrice: ''
    };
  }
};
</script>

<style scoped>
/* Main Container */
.seat-price-container {
  width: 658px;
  height: 490px;
  position: relative;
  /* background: rgba(255, 255, 255, 0); -> Transparent background */
  overflow: hidden;
  border-radius: 20px;
}

/* Positioning for the inner content area, based on original offsets */
.content-area {
  width: 658px;
  height: 490px;
  position: absolute;
  left: 27px;
  top: 9px;
}

/* White Card/Form Background */
.card-background {
  width: 591px;
  height: 264px;
  position: absolute;
  left: 24px;
  top: 18px;
  background: white;
  box-shadow: 0px 0px 8px rgba(194, 198, 204, 0.60);
  border-radius: 10px;
}

/* Title */
.title {
  width: 187px;
  height: 33px;
  position: absolute;
  left: 77px;
  top: 38px;
  color: #484848;
  font-size: 18px;
  font-family: Montserrat, sans-serif;
  font-weight: 700;
  white-space: nowrap;
}

/* Input Wrapper & Input Field Styling */
.price-input-wrapper {
  position: absolute;
  left: 46px;
  top: 114px;
}

.price-input {
  width: 536px;
  height: 60px;
  padding: 0 15px; /* Added padding for text */
  background: white;
  border-radius: 30px;
  border: 1px #C2C6CC solid;
  font-size: 16px;
  box-sizing: border-box; /* Include padding and border in the element's total width and height */
  position: absolute;
  top: 0;
  left: 0;
  /* Hide default browser focus outline */
  outline: none;
}

/* Input Label */
.input-label {
  position: absolute;
  left: 32px; /* 78px - 46px */
  top: 5px; /* 119px - 114px */
  color: #484848;
  font-size: 14px;
  font-family: Montserrat, sans-serif;
  font-weight: 600;
  z-index: 10; /* Ensure label is above the input/wrapper if needed */
}

/* Set Price Button */
.set-price-button {
  width: 187px;
  height: 54px;
  position: absolute;
  left: 46px;
  top: 199px;
  background: #9A9A9A;
  border-radius: 30px;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.button-text {
  color: white;
  font-size: 15px;
  font-family: Montserrat, sans-serif;
  font-weight: 700;
  white-space: nowrap;
}

/* Divider Line */
.divider {
  width: 536px;
  height: 0px;
  position: absolute;
  left: 62px;
  top: 89px;
  border-top: 1px #E0E2E6 solid;
}

/* Close Icon Placeholder */
.close-icon-placeholder {
  width: 15.01px;
  height: 15.01px;
  position: absolute;
  left: 582px;
  top: 46px;
  background: #484848; /* Placeholder color, typically replaced with an SVG or icon font */
}

/* Misc Element Placeholder */
.misc-element {
  width: 30px;
  height: 30px;
  left: 274px;
  top: 263px;
  position: absolute;
  /* Placeholder for whatever this element might be */
}
</style>