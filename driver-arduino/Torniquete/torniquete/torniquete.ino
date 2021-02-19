int boton = 2;
int torniquete = LED_BUILTIN;
int tiempo = 5000; 

void setup() {
  
  pinMode(torniquete, OUTPUT);
  pinMode(boton, INPUT);
  
  Serial.begin(9600);
}

void loop() {
  
  if (Serial.available() > 0) {
    int input = Serial.read();
      if (input == '1') {
        
        digitalWrite(LED_BUILTIN, HIGH);
        delay(tiempo);
        digitalWrite(LED_BUILTIN, LOW);
      }
  }
}
