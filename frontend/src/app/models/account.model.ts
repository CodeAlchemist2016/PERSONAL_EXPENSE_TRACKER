export interface Account {
  id: number;
  accountType: string;
  balance: number;
  bankName: string;
  investmentType?: string;
  portfolioValue?: number;
  accountNumber: string;
  maxSpending?: number;
  user: {id: number; username: string};
}
