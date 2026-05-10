export type ResponseError = {
  code: string;
  message: string;
  details?: unknown;
};

export type ResponseMeta = Record<string, unknown>;

export class ResponseDto<T = unknown> {
  readonly success: boolean;
  readonly message: string;
  readonly data: T | null;
  readonly errors: ResponseError[];
  readonly meta?: ResponseMeta;
  readonly timestamp: string;

  private constructor(params: {
    success: boolean;
    message: string;
    data?: T | null;
    errors?: ResponseError[];
    meta?: ResponseMeta;
  }) {
    this.success = params.success;
    this.message = params.message;
    this.data = params.data ?? null;
    this.errors = params.errors ?? [];
    this.meta = params.meta;
    this.timestamp = new Date().toISOString();
  }

  static success<T>(message: string, data?: T, meta?: ResponseMeta): ResponseDto<T> {
    return new ResponseDto<T>({
      success: true,
      message,
      data: data ?? null,
      meta,
    });
  }

  static failure(
    message: string,
    errors: ResponseError[] = [],
    meta?: ResponseMeta,
  ): ResponseDto<null> {
    return new ResponseDto<null>({
      success: false,
      message,
      data: null,
      errors,
      meta,
    });
  }
}
