# Pseudocode

## Refresh doctors

START  
Read selected department.  
Remove all doctor items.  
FOR each doctor in `doctorsByDepartment[department]`: add doctor.  
END FOR  
END

## Validate and book

START  
Read patient ID, doctor, and time slot.  
IF patient ID is empty OR doctor is absent, show error and stop.  
Set booking-in-progress.  
Create appointment and show confirmation.  
Clear booking-in-progress.  
END
