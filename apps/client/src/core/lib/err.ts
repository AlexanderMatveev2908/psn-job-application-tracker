export class ErrApp extends Error {
  status: number;
  msg: string;

  constructor(msg: string, status: number = 500) {
    super(msg);
    this.msg = msg;
    this.status = status;

    Object.setPrototypeOf(this, ErrApp.prototype);

    this.name = "ErrApp";
  }
}
