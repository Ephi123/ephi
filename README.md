# ephi

## Standard API response DTO

All API responses should be wrapped with `ResponseDto` so success and error payloads share the same shape.

### Success response

```ts
import { ResponseDto } from './src/dto/response.dto';

return ResponseDto.success('User fetched successfully', user);
```

Response shape:

```json
{
  "success": true,
  "message": "User fetched successfully",
  "data": {
    "id": "user_123"
  },
  "errors": [],
  "timestamp": "2026-05-10T00:00:00.000Z"
}
```

### Error response

```ts
import { ResponseDto } from './src/dto/response.dto';

return ResponseDto.failure('Validation failed', [
  {
    "code": "VALIDATION_ERROR",
    "message": "Email is required"
  }
]);
```

Response shape:

```json
{
  "success": false,
  "message": "Validation failed",
  "data": null,
  "errors": [
    {
      "code": "VALIDATION_ERROR",
      "message": "Email is required"
    }
  ],
  "timestamp": "2026-05-10T00:00:00.000Z"
}
```
