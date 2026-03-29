// Hotel Reservation System - Client-side JavaScript

document.addEventListener('DOMContentLoaded', function() {

    // ===== Date Validation for Booking Forms =====
    const checkInInput = document.getElementById('checkIn');
    const checkOutInput = document.getElementById('checkOut');

    if (checkInInput && checkOutInput) {
        // Set min date to today
        const today = new Date().toISOString().split('T')[0];
        checkInInput.setAttribute('min', today);

        checkInInput.addEventListener('change', function() {
            // Set checkout min to day after checkin
            if (this.value) {
                const nextDay = new Date(this.value);
                nextDay.setDate(nextDay.getDate() + 1);
                checkOutInput.setAttribute('min', nextDay.toISOString().split('T')[0]);

                // Reset checkout if it's before new min
                if (checkOutInput.value && checkOutInput.value <= this.value) {
                    checkOutInput.value = nextDay.toISOString().split('T')[0];
                }
            }
        });

        // Calculate nights and total on the fly
        checkOutInput.addEventListener('change', function() {
            calculateTotal();
        });
        checkInInput.addEventListener('change', function() {
            calculateTotal();
        });
    }

    function calculateTotal() {
        const checkIn = document.getElementById('checkIn');
        const checkOut = document.getElementById('checkOut');
        const priceEl = document.getElementById('pricePerNight');
        const nightsEl = document.getElementById('nightsCount');
        const totalEl = document.getElementById('totalPrice');

        if (checkIn && checkOut && priceEl && checkIn.value && checkOut.value) {
            const start = new Date(checkIn.value);
            const end = new Date(checkOut.value);
            const nights = Math.ceil((end - start) / (1000 * 60 * 60 * 24));
            const price = parseFloat(priceEl.dataset.price);

            if (nights > 0 && nightsEl && totalEl) {
                nightsEl.textContent = nights + ' night' + (nights > 1 ? 's' : '');
                totalEl.textContent = '₹' + (nights * price).toLocaleString('en-IN');
            }
        }
    }

    // ===== Form Validation =====
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // ===== Password Confirm Validation =====
    const confirmPassword = document.getElementById('confirmPassword');
    const password = document.getElementById('password');
    if (confirmPassword && password) {
        confirmPassword.addEventListener('input', function() {
            if (this.value !== password.value) {
                this.setCustomValidity('Passwords do not match');
            } else {
                this.setCustomValidity('');
            }
        });
    }

    // ===== Auto-dismiss alerts after 5 seconds =====
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 5000);
    });

    // ===== Guest count validation against room capacity =====
    const guestsInput = document.getElementById('guests');
    const capacityEl = document.getElementById('roomCapacity');
    if (guestsInput && capacityEl) {
        const maxCapacity = parseInt(capacityEl.dataset.capacity);
        guestsInput.setAttribute('max', maxCapacity);
        guestsInput.addEventListener('change', function() {
            if (parseInt(this.value) > maxCapacity) {
                this.value = maxCapacity;
                alert('Maximum capacity for this room is ' + maxCapacity + ' guests.');
            }
        });
    }

    // ===== Smooth scroll =====
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth' });
            }
        });
    });
});
