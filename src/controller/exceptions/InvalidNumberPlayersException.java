package controller.exceptions;

public class InvalidNumberPlayersException extends Exception {


  public InvalidNumberPlayersException() {
    super();

  }

  @Override
  public String getMessage() {
    return "The number of Autoplayers must be between 1 and 7";
  }
}

